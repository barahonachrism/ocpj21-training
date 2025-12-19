# Java SE 21 Developer Study Guide
Study guide for the Java SE 21 Developer Exam (1Z0-830).

## Download Book
You can download the PDF version of the book here: [ocpj21-book.pdf](ocpj21-book.pdf)

## PDF Generation
If you want to regenerate the PDF from the source Markdown files, follow these steps:

### Prerequisites
- **Pandoc**: A universal document converter.
- **BasicTeX** (or MacTeX/TeX Live): Required for `xelatex` to generate PDFs.
- **Python 3**: Used for preprocessing the Markdown files.

### Steps
1. Ensure you have the prerequisites installed. On macOS, you can use Homebrew:
   ```bash
   brew install pandoc basictex python
   ```
   *Note: After installing BasicTeX, you might need to restart your terminal or add the bin to your PATH.*

2. Run the generation script:
   ```bash
   ./generate_pdf.sh
   ```
   This script will preprocess the files and use Pandoc to generate `ocpj21-book.pdf`.

# Buying
You can read the book for free [here](http://ocpj21.javastudyguide.com/), but if you like the content and want to support me, you can buy the print and e-book version:

SOON...

# Exam Simulator
This project now includes a full-stack Exam Simulator to practice for the 1Z0-830 certification.

## Prerequisites
- Java 21
- Maven
- Node.js & npm
- Angular CLI

## Backend Setup
1. Navigate to the backend directory:
   ```bash
   cd simulator/backend
   ```
2. Run the application:
   ```bash
   mvn spring-boot:run
   ```
   The backend will start on `http://localhost:8080`.

## Frontend Setup
1. Navigate to the frontend directory:
   ```bash
   cd simulator/frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the application:
   ```bash
   ng serve
   ```
   Navigate to `http://localhost:4200/`.

# Product Update Summary
**New Feature: Interactive Exam Simulator**
- **Real-time Simulation**: Added a complete exam simulation environment mimicking the real test conditions.
- **Timer Integration**: 120-minute countdown timer to track exam duration.
- **Instant Feedback**: Detailed results page showing score, pass/fail status, and explanations for every question.
- **Question Bank**: Integrated 50 practice questions covering key Java 21 topics.
- **User Interface**: Modern, responsive design for a better study experience.

# Contributing
Any contributions are welcomed. Please have a look at [CONTRIBUTING.md](https://github.com/eh3rrera/ocpj21-book/blob/master/CONTRIBUTING.md).


# License
&copy;2024 by Esteban Herrera.
Exam Simulator, PDF generation, and additional contributions by Christian Barahona.

Oracle and Java are registered trademarks of Oracle and/or its affiliates. Other names may be trademarks of their respective owners.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text" rel="dct:type">work</span> is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.