# HealthAnalyzer

**HealthAnalyzer** is a full-stack AI-assisted medical analysis system. It combines a **Spring Boot 3.5.6 (Java 21)** backend and a **React (Vite + TailwindCSS)** frontend to let users upload CT/MRI images and blood-test reports (JPEG/PNG/PDF), extract text via OCR / PDFBox, analyze content with AI models (OpenAI / OpenRouter / Claude), and view historical results. Authentication uses JWT and the system stores per-user file & analysis history.

Designed for clinicians and patients to accelerate triage and provide clear, plain-language summaries.  
Supports re-analysis, per-user audit trails, and admin user management for quality control and compliance.  
Uses Tess4J (Tesseract) and Apache PDFBox for OCR/PDF parsing — ensure `eng.traineddata` is available on the server.  
AI analysis is pluggable: swap `ChatGptService`, `OpenRouterService` or other implementations without changing core logic.  
Default dev DB is H2; easily switch to PostgreSQL or MySQL and supply API keys via secure environment variables.

---

## Table of Contents

- [Features](#features)  
- [Requirements](#requirements)  
- [Architecture & Folder Structure](#architecture--folder-structure)  
- [Quick start (run locally)](#quick-start-run-locally)  
  - [Backend (server)](#backend-server)  
  - [Frontend (client)](#frontend-client)  
- [Configuration](#configuration)  
  - [Tesseract / Tess4J OCR data](#tesseract--tess4j-ocr-data)  
  - [AI provider keys (OpenAI / OpenRouter)](#ai-provider-keys-openai--openrouter)  
  - [Database (H2 => production DB)](#database-h2---production-db)  
- [How file analysis works (overview)](#how-file-analysis-works-overview)  
- [Important API endpoints (examples)](#important-api-endpoints-examples)  
- [Build & produce single JAR that serves frontend](#build--produce-single-jar-that-serves-frontend)  
- [Git / repo hygiene tips](#git--repo-hygiene-tips)  
- [`.gitignore` recommended snippet](#gitignore-recommended-snippet)  
- [Extending / swapping AI services](#extending--swapping-ai-services)  
- [Development history & common commands](#development-history--common-commands)  
- [Security notes](#security-notes)  
- [License & Contact](#license--contact)

---

## Features

- 🔐 JWT-based auth: register, login, roles (user / admin).  
- 🩻 File uploads: JPEG/PNG/PDF (CT, MRI, blood reports).  
- 🧾 OCR extraction using **Tess4J** (Tesseract).  
- 📄 PDF parsing using **Apache PDFBox**.  
- 🤖 AI text analysis via pluggable services (OpenAI, OpenRouter / Anthropic Claude).  
- 📚 Per-user file & analysis history; re-analysis of saved files.  
- ⚙️ Admin area to manage users & audits.  
- 🧪 H2 in-memory DB for dev; configurable to Postgres/MySQL for prod.

---

## Requirements

- **Java** 21  
- **Spring Boot** 3.5.6  
- **Maven** 3.8+ (or use included `mvnw`/`mvnw.cmd`)  
- **Node.js** v22.16.0  
- **npm** 10.9.2  
- **Tesseract OCR** installed with `eng.traineddata` for Tess4J  
- OpenAI / OpenRouter API keys (if using remote AI models)

---

## Architecture & Folder Structure

```
HealthAnalyzer/
├── client/        # React frontend (Vite + Tailwind)
│   ├── src/
│   ├── public/
│   └── package.json
│
├── server/        # Spring Boot backend
│   ├── src/
│   ├── pom.xml
│   ├── mvnw
│   └── .mvn/
│
├── .gitignore
└── README.md
```

---

## Quick start (run locally)

Clone repository:

```bash
git clone https://github.com/<yourusername>/HealthAnalyzer.git
cd HealthAnalyzer
```

### Backend (server)

From project root:

```bash
cd server

# Build (Linux/macOS)
./mvnw clean package

# or (Windows)
mvnw.cmd clean package

# Run (dev)
./mvnw spring-boot:run
# or (Windows)
mvnw.cmd spring-boot:run
```

Default server URL: `http://localhost:8080`

### Frontend (client)

From project root:

```bash
cd client
npm install
npm run dev
```

Vite dev server: typically `http://localhost:5173`

To proxy frontend API calls to backend in development, add to `client/package.json`:

```json
"proxy": "http://localhost:8080"
```

---

## Configuration

### Tesseract / Tess4J OCR data

Tess4J requires the Tesseract trained data file `eng.traineddata`. Put that file where Tess4J/Tesseract can access it. Example path on Windows (as used on your machine):

```
D:\ocr\eng.traineddata
```

Make sure your app config points to the correct `tessdata` directory, for example in `application.yml`:

```yaml
tesseract:
  tessdata-path: "D:\ocr"   # Windows example; on Linux: /usr/share/tessdata
```

You can also set `TESSDATA_PREFIX` as an environment variable.

### AI provider keys (OpenAI / OpenRouter)

Store secrets in environment variables or a `.env` (gitignored). Example `server/src/main/resources/application.yml` snippet:

```yaml
openai:
  api:
    key: "${OPENAI_API_KEY:}"

openrouter:
  api-key: "${OPENROUTER_API_KEY:}"
  base-url: https://openrouter.ai/api/v1/chat/completions
  model: anthropic/claude-3-haiku
```

**Never check in real keys.** Use env vars or a secrets manager in production.

### Database (H2 => production DB)

Default: H2 in-memory DB. To switch to Postgres or MySQL, update `spring.datasource.*`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/healthdb
    username: youruser
    password: yourpass
  jpa:
    hibernate:
      ddl-auto: update
```

---

## How file analysis works (overview)

1. User uploads file via `/api/files/upload`.  
2. Backend stores file and extracts text:
   - Images → Tess4J OCR (Tesseract `eng.traineddata`)  
   - PDFs → Apache PDFBox text extraction  
3. Extracted text is passed to `FileAnalysisService`.  
4. `FileAnalysisService` calls the injected text analysis service (`ChatGptService` or `OpenRouterService`), e.g.:

```java
private String analyzeText(String text) {
    if (text == null || text.isBlank()) return "No content provided.";
    return openRouterService.chat(text);
}
```

5. Analysis stored in DB and returned to user; user can re-run analysis later.

---

## Important API endpoints (examples)

> Use `Authorization: Bearer <token>` header for protected endpoints (where applicable).

| Method | Endpoint                                 | Description |
|--------|------------------------------------------|-------------|
| POST   | `/api/auth/register`                     | Register a new user. |
| POST   | `/api/auth/login`                        | Login — returns JWT token. |

| Method | Endpoint                                 | Description |
|--------|------------------------------------------|-------------|
| GET    | `/api/file-analyses/{analysesId}`        | Retrieve a file analysis result by analysis ID. |
| GET    | `/api/file-analyses/document/{documentId}` | Retrieve analysis results associated with a specific document ID. |
| POST   | `/api/file-analyses`                     | Create a new file analysis (kick off analysis for stored/extracted text or link to an uploaded file). |

| Method | Endpoint                                 | Description |
|--------|------------------------------------------|-------------|
| POST (multipart/form-data) | `/api/upload`                 | Upload a file (image/pdf). Accepts multipart form-data. Returns uploaded file metadata / id. |
| POST (multipart/form-data) | `/api/upload/reportExplain`   | Upload a report (image/pdf) and request an explanatory analysis in a single call. Consumes multipart form-data. |

| Method | Endpoint                                 | Description |
|--------|------------------------------------------|-------------|
| GET    | `/api/user-activities/user/{userId}`     | Get activity history (uploads, analyses, re-analyses) for a specific user. |

| Method | Endpoint                                 | Description |
|--------|------------------------------------------|-------------|
| POST   | `/api/user-profiles`                     | Create a new user profile (profile metadata separate from auth). |
| GET    | `/api/user-profiles/{id}`                | Get a user profile by ID. |
| PUT    | `/api/user-profiles/{id}`                | Update a user profile by ID. |
| DELETE | `/api/user-profiles/{id}`                | Delete a user profile by ID. |


---

## Build & produce single JAR that serves frontend

To deploy one artifact that serves the static frontend from Spring Boot:

1. Build frontend for production:

```bash
cd client
npm run build
# Vite outputs to `dist/` by default
```

2. Copy build files into Spring Boot static resources:

```bash
# from project root
cp -r client/dist/* server/src/main/resources/static/
```

3. Build Spring Boot jar:

```bash
cd server
./mvnw clean package
# The produced jar in server/target/ will serve the React files
java -jar target/*.jar
```

---

## Git / repo hygiene tips

- Add `.gitignore` *before* committing build artifacts or secrets.  
- To stop tracking files that were already committed (keeps local copy), run:

```bash
git rm -r --cached client/node_modules server/target server/.idea .env
git add .gitignore
git commit -m "Remove tracked ignored files and update .gitignore"
git push
```

- If a secret was pushed to remote, rotate that secret immediately and remove it from history (use `git filter-repo` or `bfg-repo-cleaner`) — this rewrites git history and requires force push.

- Change last commit message (if not yet pushed, or if you will force-push):
```bash
git commit --amend -m "New commit message"
git push --force   # only if you understand history rewrite effects
```

---

## `.gitignore` recommended snippet

Place this in repo root `.gitignore`:

```gitignore
# Server (Spring Boot)
server/target/
server/*.jar
server/*.war
server/*.iml
server/.idea/
server/logs/
server/*.log

# Client (React / Vite)
client/node_modules/
client/dist/
client/build/

# Common
.env
.env.*
.DS_Store
Thumbs.db
*.log
```

---

## Extending / swapping AI services

- `TextAnalysisService` interface pattern recommended:

```java
public interface TextAnalysisService {
    String chat(String text);
}
```

- Example implementations:
  - `ChatGptService` (OpenAI)
  - `OpenRouterService` (Anthropic / Claude via OpenRouter)

Inject the desired implementation into `FileAnalysisService` via Spring — swapping models becomes trivial.

---

## Development history & common commands

Commands you used / useful references:

```text
# Frontend (Vite + Tailwind)
npm create vite@latest ai-med-frontend --template react
cd ai-med-frontend
npm install
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p
npm install axios@1.9.0 react-router-dom
npm run dev
npm run build
npm run preview

# Useful Windows debug
netstat -ano | findstr :5173
taskkill /PID <pid> /F
doskey /HISTORY > history.txt
```

---

## Security notes

- **Never commit** API keys or `.env` files.  
- Rotate any keys accidentally committed.  
- Use environment variables or a secrets manager in production.  
- Use HTTPS and properly configure CORS for production.

---

## License & Contact

This project is released under the **MIT License**.

**Author:** Moh Khandan
**Email:** khandanmoh@gmail.com  
**GitHub:** https://github.com/moksnow