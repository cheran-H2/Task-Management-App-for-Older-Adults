import os
import subprocess
import sys

def main():
    base_dir = os.path.abspath(os.path.dirname(__file__))
    html_file = os.path.join(base_dir, "docs", "architecture_and_implementation_report.html")
    pdf_file = os.path.join(base_dir, "ElderCare_Technical_Architecture_and_Implementation_Guide.pdf")

    if not os.path.exists(html_file):
        print(f"Error: HTML template not found at {html_file}")
        sys.exit(1)

    # Locate system browser for native headless PDF printing
    browser_candidates = [
        r"C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe",
        r"C:\Program Files\Microsoft\Edge\Application\msedge.exe",
        r"C:\Program Files\Google\Chrome\Application\chrome.exe",
        r"C:\Program Files (x86)\Google\Chrome\Application\chrome.exe"
    ]

    browser_path = None
    for candidate in browser_candidates:
        if os.path.exists(candidate):
            browser_path = candidate
            break

    if not browser_path:
        print("Error: Neither Microsoft Edge nor Google Chrome found for headless PDF generation.")
        sys.exit(1)

    print(f"Using browser: {browser_path}")
    print(f"Source HTML: {html_file}")
    print(f"Target PDF: {pdf_file}")

    cmd = [
        browser_path,
        "--headless=new",
        "--disable-gpu",
        f"--print-to-pdf={pdf_file}",
        "--no-pdf-header-footer",
        html_file
    ]

    result = subprocess.run(cmd, capture_output=True, text=True)
    if os.path.exists(pdf_file) and os.path.getsize(pdf_file) > 0:
        size_kb = round(os.path.getsize(pdf_file) / 1024, 2)
        print(f"SUCCESS: PDF generated successfully! ({size_kb} KB)")
        
        # Also save copy in docs/
        docs_copy = os.path.join(base_dir, "docs", "ElderCare_Technical_Architecture_and_Implementation_Guide.pdf")
        with open(pdf_file, 'rb') as f_src, open(docs_copy, 'wb') as f_dst:
            f_dst.write(f_src.read())
        print(f"Copy saved to: {docs_copy}")
    else:
        print(f"PDF generation failed: {result.stderr or result.stdout}")
        sys.exit(1)

if __name__ == "__main__":
    main()
