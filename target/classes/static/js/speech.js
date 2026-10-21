/**
 * Web Speech API Integration for Older Adults
 * 1. Text-to-Speech (TTS): Reads task titles, times, and medication notes aloud clearly.
 * 2. Speech-to-Text (STT): Enables voice dictation for adding tasks without typing.
 */

class SeniorSpeechAssistant {
    constructor() {
        this.synth = window.speechSynthesis || null;
        this.speaking = false;
        this.rate = 0.85; // Slower, clearer cadence for older adults
        this.enabled = true;
        this.recognition = null;
        this.isListening = false;

        this.initRecognition();
    }

    initRecognition() {
        const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
        if (SpeechRecognition) {
            this.recognition = new SpeechRecognition();
            this.recognition.continuous = false;
            this.recognition.interimResults = false;
            this.recognition.lang = 'en-US';
        }
    }

    setEnabled(val) {
        this.enabled = !!val;
    }

    setRate(rate) {
        const r = parseFloat(rate);
        if (!isNaN(r) && r >= 0.5 && r <= 1.5) {
            this.rate = r;
        }
    }

    speak(text, onComplete) {
        if (!this.enabled || !this.synth) return;

        // Cancel any ongoing speech first
        this.stopSpeaking();

        const utterance = new SpeechSynthesisUtterance(text);
        utterance.rate = this.rate;
        utterance.pitch = 1.0;
        utterance.volume = 1.0;

        // Pick a natural, warm English voice if available
        const voices = this.synth.getVoices();
        const preferredVoice = voices.find(v => 
            v.lang.startsWith('en') && (v.name.includes('Natural') || v.name.includes('Google') || v.name.includes('Samantha') || v.name.includes('David'))
        ) || voices.find(v => v.lang.startsWith('en'));

        if (preferredVoice) {
            utterance.voice = preferredVoice;
        }

        utterance.onstart = () => {
            this.speaking = true;
            document.body.classList.add('audio-speaking');
        };

        utterance.onend = () => {
            this.speaking = false;
            document.body.classList.remove('audio-speaking');
            if (typeof onComplete === 'function') onComplete();
        };

        utterance.onerror = () => {
            this.speaking = false;
            document.body.classList.remove('audio-speaking');
        };

        this.synth.speak(utterance);
    }

    stopSpeaking() {
        if (this.synth) {
            this.synth.cancel();
            this.speaking = false;
            document.body.classList.remove('audio-speaking');
        }
    }

    startListening(onResult, onEnd, onError) {
        if (!this.recognition) {
            if (onError) onError('Speech recognition is not supported in this browser. You can type your task in the box.');
            return;
        }

        if (this.isListening) {
            this.recognition.stop();
            return;
        }

        this.recognition.onstart = () => {
            this.isListening = true;
        };

        this.recognition.onresult = (event) => {
            const transcript = event.results[0][0].transcript;
            if (typeof onResult === 'function') {
                onResult(transcript);
            }
        };

        this.recognition.onerror = (event) => {
            this.isListening = false;
            if (typeof onError === 'function') {
                onError(event.error);
            }
        };

        this.recognition.onend = () => {
            this.isListening = false;
            if (typeof onEnd === 'function') {
                onEnd();
            }
        };

        try {
            this.recognition.start();
        } catch (e) {
            this.isListening = false;
            if (onError) onError(e.message);
        }
    }

    stopListening() {
        if (this.recognition && this.isListening) {
            this.recognition.stop();
            this.isListening = false;
        }
    }
}

window.seniorSpeech = new SeniorSpeechAssistant();
