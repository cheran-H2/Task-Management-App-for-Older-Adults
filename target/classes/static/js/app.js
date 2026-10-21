/**
 * Task Management Application for Older Adults
 * Core Frontend Application Logic
 */

document.addEventListener('DOMContentLoaded', () => {
    // Current State
    let currentUser = {
        userId: 1,
        fullName: 'Eleanor Vance',
        email: 'eleanor@eldercare.test',
        phoneNumber: '(555) 234-5678',
        emergencyContactName: 'Sarah Vance (Daughter)',
        emergencyContactPhone: '(555) 876-5432'
    };

    let currentFilter = 'TODAY';
    let currentPriority = 'ALL';
    let tasksList = [];
    let preferences = {
        fontSize: 'large',
        theme: 'cream',
        speechEnabled: true,
        speechSpeed: 0.85,
        soundEffectsEnabled: true,
        simplifiedView: false
    };

    let lastCompletedTaskId = null;
    let reminderCheckInterval = null;

    // DOM Elements
    const elements = {
        body: document.body,
        userNameGreeting: document.getElementById('user-name-greeting'),
        userPillName: document.getElementById('user-pill-name'),
        userAvatarLetter: document.getElementById('user-avatar-letter'),
        heroMessage: document.getElementById('hero-message'),
        todayDateLabel: document.getElementById('today-date-label'),
        statTodayCompleted: document.getElementById('stat-today-completed'),
        statTodayTotal: document.getElementById('stat-today-total'),
        statRate: document.getElementById('stat-rate'),
        taskGrid: document.getElementById('task-grid'),
        filterTabs: document.querySelectorAll('.tab-btn'),
        reminderBanner: document.getElementById('reminder-banner'),
        reminderTitle: document.getElementById('reminder-title'),
        reminderDesc: document.getElementById('reminder-desc'),
        reminderSnoozeBtn: document.getElementById('reminder-snooze-btn'),
        reminderDoneBtn: document.getElementById('reminder-done-btn'),
        fontSizeBtns: document.querySelectorAll('[data-font]'),
        themeBtns: document.querySelectorAll('[data-theme-btn]'),
        speechToggleBtn: document.getElementById('speech-toggle-btn'),
        newTaskBtn: document.getElementById('new-task-btn'),
        taskModal: document.getElementById('task-modal'),
        taskModalClose: document.getElementById('task-modal-close'),
        taskForm: document.getElementById('task-form'),
        modalTitle: document.getElementById('modal-title'),
        voiceDictateBtn: document.getElementById('voice-dictate-btn'),
        usabilityBanner: document.getElementById('usability-banner'),
        usabilityModal: document.getElementById('usability-modal'),
        usabilityCloseBtn: document.getElementById('usability-close-btn'),
        authModal: document.getElementById('auth-modal'),
        authCloseBtn: document.getElementById('auth-close-btn'),
        userPill: document.getElementById('user-pill'),
        loginForm: document.getElementById('login-form'),
        demoLoginBtn: document.getElementById('demo-login-btn'),
        emergencyCallBtn: document.getElementById('emergency-call-btn'),
        emergencyName: document.getElementById('emergency-name'),
        emergencyPhone: document.getElementById('emergency-phone')
    };

    // Initialize Application
    init();

    async function init() {
        setupCurrentDate();
        loadSavedLocalPreferences();
        await fetchUserProfile();
        await refreshAll();
        setupEventListeners();
        startReminderMonitor();
    }

    function setupCurrentDate() {
        const today = new Date();
        const options = { weekday: 'long', month: 'long', day: 'numeric', year: 'numeric' };
        if (elements.todayDateLabel) {
            elements.todayDateLabel.textContent = today.toLocaleDateString('en-US', options);
        }
    }

    function loadSavedLocalPreferences() {
        const savedTheme = localStorage.getItem('eldercare_theme') || 'cream';
        const savedFont = localStorage.getItem('eldercare_font') || 'large';
        applyTheme(savedTheme);
        applyFontSize(savedFont);
    }

    function applyTheme(theme) {
        preferences.theme = theme;
        elements.body.setAttribute('data-theme', theme);
        localStorage.setItem('eldercare_theme', theme);
        elements.themeBtns.forEach(btn => {
            btn.classList.toggle('active', btn.getAttribute('data-theme-btn') === theme);
        });
    }

    function applyFontSize(size) {
        preferences.fontSize = size;
        elements.body.setAttribute('data-font-size', size);
        localStorage.setItem('eldercare_font', size);
        elements.fontSizeBtns.forEach(btn => {
            btn.classList.toggle('active', btn.getAttribute('data-font') === size);
        });
    }

    async function fetchUserProfile() {
        try {
            const res = await fetch(`/api/auth/me?userId=${currentUser.userId}`);
            if (res.ok) {
                const data = await res.json();
                currentUser.fullName = data.fullName;
                currentUser.email = data.email;
                currentUser.phoneNumber = data.phoneNumber;
                currentUser.emergencyContactName = data.emergencyContactName;
                currentUser.emergencyContactPhone = data.emergencyContactPhone;

                if (data.preferences) {
                    preferences = Object.assign(preferences, data.preferences);
                    applyTheme(preferences.theme || 'cream');
                    applyFontSize(preferences.fontSize || 'large');
                    if (window.seniorSpeech) {
                        window.seniorSpeech.setEnabled(preferences.speechEnabled);
                        window.seniorSpeech.setRate(preferences.speechSpeed);
                    }
                    if (window.soundEffects) {
                        window.soundEffects.setEnabled(preferences.soundEffectsEnabled);
                    }
                }
                updateUserDisplay();
            }
        } catch (e) {
            console.log('Using default user profile:', e);
            updateUserDisplay();
        }
    }

    function updateUserDisplay() {
        const firstName = currentUser.fullName.split(' ')[0] || 'Friend';
        if (elements.userNameGreeting) {
            const hour = new Date().getHours();
            let greetingTime = 'Good morning';
            if (hour >= 12 && hour < 17) greetingTime = 'Good afternoon';
            else if (hour >= 17) greetingTime = 'Good evening';
            elements.userNameGreeting.textContent = `${greetingTime}, ${firstName}!`;
        }
        if (elements.userPillName) elements.userPillName.textContent = currentUser.fullName;
        if (elements.userAvatarLetter) elements.userAvatarLetter.textContent = firstName.charAt(0);
        if (elements.emergencyName) elements.emergencyName.textContent = currentUser.emergencyContactName || 'Family Member';
        if (elements.emergencyPhone) elements.emergencyPhone.textContent = currentUser.emergencyContactPhone || '(555) 000-0000';
        if (elements.emergencyCallBtn) {
            elements.emergencyCallBtn.href = `tel:${currentUser.emergencyContactPhone || ''}`;
        }
    }

    async function refreshAll() {
        await fetchTasks();
        await fetchStats();
    }

    async function fetchTasks() {
        try {
            let url = `/api/tasks?userId=${currentUser.userId}&filter=${currentFilter}`;
            if (currentPriority !== 'ALL') {
                url += `&priority=${currentPriority}`;
            }
            const res = await fetch(url);
            if (res.ok) {
                tasksList = await res.json();
                renderTasks();
            }
        } catch (e) {
            console.error('Error fetching tasks:', e);
        }
    }

    async function fetchStats() {
        try {
            const res = await fetch(`/api/tasks/stats?userId=${currentUser.userId}`);
            if (res.ok) {
                const stats = await res.json();
                if (elements.statTodayCompleted) elements.statTodayCompleted.textContent = stats.todayCompleted;
                if (elements.statTodayTotal) elements.statTodayTotal.textContent = stats.todayTotal;
                if (elements.statRate) elements.statRate.textContent = `${stats.todayCompletionRate}%`;
                if (elements.heroMessage && stats.encouragingMessage) {
                    elements.heroMessage.textContent = stats.encouragingMessage;
                }
            }
        } catch (e) {
            console.error('Error fetching stats:', e);
        }
    }

    function renderTasks() {
        if (!elements.taskGrid) return;
        elements.taskGrid.innerHTML = '';

        if (tasksList.length === 0) {
            elements.taskGrid.innerHTML = `
                <div style="background: var(--bg-surface); border: 2px dashed var(--border-strong); border-radius: var(--radius-md); padding: 48px 24px; text-align: center;">
                    <div style="font-size: 3rem; margin-bottom: 12px;">🌱</div>
                    <h3 style="font-size: var(--font-subheading); margin-bottom: 6px;">No tasks right now</h3>
                    <p style="color: var(--text-secondary); margin-bottom: 20px;">You are all caught up! Would you like to add a new task or reminder?</p>
                    <button class="primary-btn" onclick="document.getElementById('new-task-btn').click()">
                        <span>➕</span> Add a New Task
                    </button>
                </div>
            `;
            return;
        }

        tasksList.forEach(task => {
            const card = document.createElement('div');
            card.className = `task-card ${task.completed ? 'is-completed' : ''}`;
            card.setAttribute('data-id', task.id);

            const priorityBadgeClass = task.priority ? task.priority.toLowerCase() : 'medium';
            const priorityText = task.priority ? `${task.priority} Priority` : 'Routine';

            let dueTimeFormatted = '';
            if (task.dueTime) {
                try {
                    const [h, m] = task.dueTime.split(':');
                    const hour = parseInt(h, 10);
                    const ampm = hour >= 12 ? 'PM' : 'AM';
                    const hour12 = hour % 12 || 12;
                    dueTimeFormatted = `${hour12}:${m} ${ampm}`;
                } catch (e) {
                    dueTimeFormatted = task.dueTime;
                }
            }

            const categoryEmojiMap = {
                'MEDICATION': '💊',
                'APPOINTMENT': '🩺',
                'HEALTH': '🚶',
                'ROUTINE': '🏡',
                'SOCIAL': '📞',
                'CHORE': '🛒',
                'LEISURE': '📖'
            };
            const categoryEmoji = categoryEmojiMap[task.category] || '📋';

            card.innerHTML = `
                <button class="check-btn" title="${task.completed ? 'Mark as Not Completed' : 'Mark as Completed'}" aria-label="Mark task done">
                    ${task.completed ? '✓' : ''}
                </button>

                <div class="task-info">
                    <div class="task-header-row">
                        <span class="badge-priority ${priorityBadgeClass}">
                            ${task.priority === 'HIGH' ? '❤️' : task.priority === 'MEDIUM' ? '⏱️' : '✨'}
                            ${priorityText}
                        </span>
                        <span class="badge-category">
                            ${categoryEmoji} ${task.category || 'Routine'}
                        </span>
                        ${task.recurringPattern && task.recurringPattern !== 'NONE' ? `<span class="badge-category">🔄 Repeats ${task.recurringPattern.toLowerCase()}</span>` : ''}
                    </div>

                    <h3 class="task-title">${escapeHtml(task.title)}</h3>
                    
                    ${task.description ? `<p class="task-desc">${escapeHtml(task.description)}</p>` : ''}
                    ${task.notes ? `<div class="task-notes"><strong>Note:</strong> ${escapeHtml(task.notes)}</div>` : ''}

                    <div class="task-meta">
                        ${dueTimeFormatted ? `<span>⏰ Due: ${dueTimeFormatted}</span>` : ''}
                        <span>📅 ${task.dueDate || 'Today'}</span>
                        ${task.reminderEnabled ? `<span>🔔 Reminder On</span>` : ''}
                    </div>
                </div>

                <div class="task-actions">
                    <button class="action-icon-btn speak-btn" title="Read task aloud" aria-label="Listen to task description">
                        🔊
                    </button>
                    <button class="action-icon-btn edit-btn" title="Edit task" aria-label="Edit task">
                        ✏️
                    </button>
                    <button class="action-icon-btn delete-btn" title="Delete task" aria-label="Delete task">
                        🗑️
                    </button>
                </div>
            `;

            // Event Listeners for this card
            const checkBtn = card.querySelector('.check-btn');
            checkBtn.addEventListener('click', () => toggleTask(task.id));

            const speakBtn = card.querySelector('.speak-btn');
            speakBtn.addEventListener('click', () => speakTask(task));

            const editBtn = card.querySelector('.edit-btn');
            editBtn.addEventListener('click', () => openEditModal(task));

            const deleteBtn = card.querySelector('.delete-btn');
            deleteBtn.addEventListener('click', () => confirmDelete(task));

            elements.taskGrid.appendChild(card);
        });
    }

    async function toggleTask(taskId) {
        if (window.soundEffects) window.soundEffects.playClick();
        try {
            const res = await fetch(`/api/tasks/${taskId}/toggle?userId=${currentUser.userId}`, {
                method: 'PATCH'
            });
            if (res.ok) {
                const updated = await res.json();
                if (updated.completed) {
                    if (window.soundEffects) window.soundEffects.playSuccess();
                    lastCompletedTaskId = taskId;
                    showToast(`Great job! Marked "${updated.title}" as completed.`, true);
                } else {
                    showToast(`Reopened "${updated.title}".`);
                }
                await refreshAll();
            }
        } catch (e) {
            console.error('Error toggling task:', e);
        }
    }

    function speakTask(task) {
        if (!window.seniorSpeech) return;
        let text = `Task: ${task.title}. `;
        if (task.priority === 'HIGH') {
            text += `This is a high priority task. `;
        }
        if (task.dueTime) {
            text += `Due at ${task.dueTime}. `;
        }
        if (task.description) {
            text += `Details: ${task.description}. `;
        }
        if (task.notes) {
            text += `Doctor or family note: ${task.notes}. `;
        }
        if (task.completed) {
            text += `This task is already completed.`;
        }

        window.seniorSpeech.speak(text);
    }

    async function confirmDelete(task) {
        const confirmed = confirm(`Are you sure you want to delete this task:\n\n"${task.title}"?`);
        if (confirmed) {
            try {
                const res = await fetch(`/api/tasks/${task.id}?userId=${currentUser.userId}`, {
                    method: 'DELETE'
                });
                if (res.ok) {
                    showToast('Task removed.');
                    await refreshAll();
                }
            } catch (e) {
                console.error('Error deleting task:', e);
            }
        }
    }

    function setupEventListeners() {
        // Tab Filtering
        elements.filterTabs.forEach(tab => {
            tab.addEventListener('click', () => {
                elements.filterTabs.forEach(t => t.classList.remove('active'));
                tab.classList.add('active');
                currentFilter = tab.getAttribute('data-filter');
                fetchTasks();
            });
        });

        // Priority Filter Dropdown or Quick Buttons if present
        const priorityFilter = document.getElementById('priority-filter-select');
        if (priorityFilter) {
            priorityFilter.addEventListener('change', (e) => {
                currentPriority = e.target.value;
                fetchTasks();
            });
        }

        // Theme Switchers
        elements.themeBtns.forEach(btn => {
            btn.addEventListener('click', () => {
                const theme = btn.getAttribute('data-theme-btn');
                applyTheme(theme);
                syncPreference({ theme });
            });
        });

        // Font Sizing Switchers
        elements.fontSizeBtns.forEach(btn => {
            btn.addEventListener('click', () => {
                const size = btn.getAttribute('data-font');
                applyFontSize(size);
                syncPreference({ fontSize: size });
            });
        });

        // Speech Toggle
        if (elements.speechToggleBtn) {
            elements.speechToggleBtn.addEventListener('click', () => {
                preferences.speechEnabled = !preferences.speechEnabled;
                if (window.seniorSpeech) window.seniorSpeech.setEnabled(preferences.speechEnabled);
                elements.speechToggleBtn.classList.toggle('active', preferences.speechEnabled);
                showToast(`Voice Reading is now ${preferences.speechEnabled ? 'Turned ON' : 'Turned OFF'}`);
                syncPreference({ speechEnabled: preferences.speechEnabled });
            });
        }

        // Modal Controls
        if (elements.newTaskBtn) {
            elements.newTaskBtn.addEventListener('click', () => openNewTaskModal());
        }
        if (elements.taskModalClose) {
            elements.taskModalClose.addEventListener('click', () => closeTaskModal());
        }
        if (elements.taskModal) {
            elements.taskModal.addEventListener('click', (e) => {
                if (e.target === elements.taskModal) closeTaskModal();
            });
        }

        // Voice Dictation
        if (elements.voiceDictateBtn && window.seniorSpeech) {
            elements.voiceDictateBtn.addEventListener('click', () => {
                const titleInput = document.getElementById('task-title-input');
                elements.voiceDictateBtn.classList.add('listening');
                elements.voiceDictateBtn.title = 'Listening... Speak now';

                window.seniorSpeech.startListening(
                    (text) => {
                        if (titleInput) {
                            titleInput.value = text;
                            titleInput.focus();
                        }
                    },
                    () => {
                        elements.voiceDictateBtn.classList.remove('listening');
                        elements.voiceDictateBtn.title = 'Click to speak task title';
                    },
                    (err) => {
                        elements.voiceDictateBtn.classList.remove('listening');
                        alert('Could not hear voice input: ' + err);
                    }
                );
            });
        }

        // Form Submit
        if (elements.taskForm) {
            elements.taskForm.addEventListener('submit', handleTaskFormSubmit);
        }

        // Usability Modal
        if (elements.usabilityBanner) {
            elements.usabilityBanner.addEventListener('click', () => openUsabilityModal());
        }
        if (elements.usabilityCloseBtn) {
            elements.usabilityCloseBtn.addEventListener('click', () => closeUsabilityModal());
        }
        if (elements.usabilityModal) {
            elements.usabilityModal.addEventListener('click', (e) => {
                if (e.target === elements.usabilityModal) closeUsabilityModal();
            });
        }

        // User Profile & Login Modal
        if (elements.userPill) {
            elements.userPill.addEventListener('click', () => openAuthModal());
        }
        if (elements.authCloseBtn) {
            elements.authCloseBtn.addEventListener('click', () => closeAuthModal());
        }
        if (elements.demoLoginBtn) {
            elements.demoLoginBtn.addEventListener('click', () => {
                currentUser.userId = 1;
                fetchUserProfile().then(() => {
                    closeAuthModal();
                    refreshAll();
                    showToast('Logged in as Eleanor Vance.');
                });
            });
        }
        if (elements.loginForm) {
            elements.loginForm.addEventListener('submit', handleLogin);
        }
    }

    function openNewTaskModal() {
        if (elements.taskForm) elements.taskForm.reset();
        document.getElementById('task-id-hidden').value = '';
        if (elements.modalTitle) elements.modalTitle.textContent = 'Add a New Task or Reminder';

        // Pre-fill today's date
        const todayStr = new Date().toISOString().split('T')[0];
        document.getElementById('task-due-date').value = todayStr;

        elements.taskModal.classList.add('active');
        document.getElementById('task-title-input').focus();
    }

    function openEditModal(task) {
        if (elements.modalTitle) elements.modalTitle.textContent = 'Edit Task Details';
        document.getElementById('task-id-hidden').value = task.id;
        document.getElementById('task-title-input').value = task.title;
        document.getElementById('task-desc-input').value = task.description || '';
        document.getElementById('task-category-select').value = task.category || 'ROUTINE';
        document.getElementById('task-due-date').value = task.dueDate || '';
        document.getElementById('task-due-time').value = task.dueTime || '';
        document.getElementById('task-reminder-chk').checked = !!task.reminderEnabled;
        document.getElementById('task-recurring-select').value = task.recurringPattern || 'NONE';
        document.getElementById('task-notes-input').value = task.notes || '';

        // Radio priority
        const priorityRadio = document.querySelector(`input[name="priority"][value="${task.priority || 'MEDIUM'}"]`);
        if (priorityRadio) priorityRadio.checked = true;

        elements.taskModal.classList.add('active');
    }

    function closeTaskModal() {
        if (elements.taskModal) elements.taskModal.classList.remove('active');
    }

    async function handleTaskFormSubmit(e) {
        e.preventDefault();
        const taskId = document.getElementById('task-id-hidden').value;
        const title = document.getElementById('task-title-input').value.trim();
        const description = document.getElementById('task-desc-input').value.trim();
        const category = document.getElementById('task-category-select').value;
        const dueDate = document.getElementById('task-due-date').value;
        const dueTime = document.getElementById('task-due-time').value || null;
        const reminderEnabled = document.getElementById('task-reminder-chk').checked;
        const recurringPattern = document.getElementById('task-recurring-select').value;
        const notes = document.getElementById('task-notes-input').value.trim();

        const priorityRadio = document.querySelector('input[name="priority"]:checked');
        const priority = priorityRadio ? priorityRadio.value : 'MEDIUM';

        if (!title) {
            alert('Please give your task a name.');
            return;
        }

        const payload = {
            userId: currentUser.userId,
            title,
            description,
            category,
            priority,
            dueDate,
            dueTime,
            reminderEnabled,
            reminderMinutesBefore: 15,
            recurringPattern,
            notes
        };

        try {
            let res;
            if (taskId) {
                // Update
                res = await fetch(`/api/tasks/${taskId}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
            } else {
                // Create
                res = await fetch(`/api/tasks`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
            }

            if (res.ok) {
                closeTaskModal();
                if (window.soundEffects) window.soundEffects.playSuccess();
                showToast(taskId ? 'Task updated successfully!' : 'New task saved!');
                await refreshAll();
            } else {
                const err = await res.json();
                alert(err.error || 'Failed to save task.');
            }
        } catch (err) {
            console.error('Error saving task:', err);
            alert('Network error while saving task.');
        }
    }

    function openUsabilityModal() {
        if (elements.usabilityModal) elements.usabilityModal.classList.add('active');
    }

    function closeUsabilityModal() {
        if (elements.usabilityModal) elements.usabilityModal.classList.remove('active');
    }

    function openAuthModal() {
        if (elements.authModal) elements.authModal.classList.add('active');
    }

    function closeAuthModal() {
        if (elements.authModal) elements.authModal.classList.remove('active');
    }

    async function handleLogin(e) {
        e.preventDefault();
        const email = document.getElementById('login-email').value.trim();
        const password = document.getElementById('login-password').value.trim();

        try {
            const res = await fetch('/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });

            const data = await res.json();
            if (res.ok && data.success) {
                currentUser.userId = data.userId;
                currentUser.fullName = data.fullName;
                currentUser.email = data.email;
                currentUser.phoneNumber = data.phoneNumber;
                currentUser.emergencyContactName = data.emergencyContactName;
                currentUser.emergencyContactPhone = data.emergencyContactPhone;

                updateUserDisplay();
                closeAuthModal();
                showToast(`Welcome back, ${data.fullName}!`);
                await refreshAll();
            } else {
                alert(data.message || 'Login failed.');
            }
        } catch (err) {
            alert('Login connection error.');
        }
    }

    async function syncPreference(updatedFields) {
        try {
            await fetch(`/api/preferences?userId=${currentUser.userId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(updatedFields)
            });
        } catch (e) {
            // Local fallback already saved
        }
    }

    // Active Reminder Monitor
    function startReminderMonitor() {
        if (reminderCheckInterval) clearInterval(reminderCheckInterval);
        checkDueReminders();
        reminderCheckInterval = setInterval(checkDueReminders, 45000); // Check every 45 seconds
    }

    async function checkDueReminders() {
        try {
            const res = await fetch(`/api/tasks/reminders?userId=${currentUser.userId}`);
            if (res.ok) {
                const reminders = await res.json();
                if (reminders && reminders.length > 0) {
                    const topReminder = reminders[0];
                    showReminderBanner(topReminder);
                } else {
                    hideReminderBanner();
                }
            }
        } catch (e) {
            // silent fail
        }
    }

    function showReminderBanner(task) {
        if (!elements.reminderBanner) return;
        elements.reminderTitle.textContent = `Reminder: ${task.title}`;
        elements.reminderDesc.textContent = task.description || 'Time for this task today!';
        elements.reminderBanner.classList.add('active');

        // Play chime
        if (window.soundEffects) {
            window.soundEffects.playReminder();
        }

        // Voice alert
        if (window.seniorSpeech && preferences.speechEnabled) {
            window.seniorSpeech.speak(`Gentle reminder: Eleanor, time for ${task.title}`);
        }

        elements.reminderDoneBtn.onclick = async () => {
            await toggleTask(task.id);
            hideReminderBanner();
        };

        elements.reminderSnoozeBtn.onclick = () => {
            hideReminderBanner();
            showToast('Reminder snoozed for 15 minutes.');
        };
    }

    function hideReminderBanner() {
        if (elements.reminderBanner) {
            elements.reminderBanner.classList.remove('active');
        }
    }

    function showToast(message, allowUndo = false) {
        let toast = document.getElementById('app-toast');
        if (!toast) {
            toast = document.createElement('div');
            toast.id = 'app-toast';
            toast.style.cssText = `
                position: fixed;
                bottom: 24px;
                right: 24px;
                background: #1E293B;
                color: #FFFFFF;
                padding: 16px 22px;
                border-radius: var(--radius-md);
                box-shadow: 0 10px 25px rgba(0,0,0,0.25);
                font-size: var(--font-small);
                font-weight: 600;
                display: flex;
                align-items: center;
                gap: 16px;
                z-index: 2000;
                border: 2px solid #38BDF8;
                max-width: 420px;
            `;
            document.body.appendChild(toast);
        }

        let undoHtml = '';
        if (allowUndo && lastCompletedTaskId) {
            undoHtml = `<button id="toast-undo-btn" style="background: #38BDF8; color: #0F172A; border: none; font-weight: 800; padding: 6px 14px; border-radius: 6px; cursor: pointer;">Undo</button>`;
        }

        toast.innerHTML = `<span>${escapeHtml(message)}</span>${undoHtml}`;
        toast.style.display = 'flex';

        if (allowUndo) {
            const undoBtn = document.getElementById('toast-undo-btn');
            if (undoBtn) {
                undoBtn.onclick = async () => {
                    if (lastCompletedTaskId) {
                        await toggleTask(lastCompletedTaskId);
                        toast.style.display = 'none';
                        lastCompletedTaskId = null;
                    }
                };
            }
        }

        setTimeout(() => {
            if (toast) toast.style.display = 'none';
        }, 5000);
    }

    function escapeHtml(str) {
        if (!str) return '';
        const div = document.createElement('div');
        div.textContent = str;
        return div.innerHTML;
    }
});
