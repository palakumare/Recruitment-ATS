# Recruitment and Applicant Tracking System (ATS)

An enterprise-grade, modern, and responsive full-stack **Recruitment and Applicant Tracking System** built with **Java Spring Boot 3**, **Spring Security 6**, **Spring Data JPA**, **Thymeleaf**, **Bootstrap 5**, and **MySQL**.

---

## 🌟 Key Features & Portals

The application implements a 3-tier role-based architecture with dynamic calculations and live hiring pipelines:

### 1. 🛡️ Admin Panel (`ROLE_ADMIN`)
- **Aggregated Analytics Dashboard**: Dynamic calculation of Total Users, Total HR, Total Jobs, Total Applications, Scheduled Interviews, Selected Candidates, and Rejected Candidates.
- **Dual-Panel Switcher**: Quick access buttons to **"Open User Panel"** and **"Open HR Panel"** allowing administrators to test and inspect all perspectives directly.
- **Candidate User Management**: Search, view candidate profiles, inspect resumes, view candidate job applications, edit profile details, activate/deactivate, and delete users.
- **HR Recruiter Management**: Search, view HR recruiter profiles, edit details, activate/deactivate, and delete HR accounts.
- **Job Governance**: Search, view, edit, activate/close, and delete job postings across all organizations.
- **Applications Oversight**: Real-time tracking of candidate names, job roles, companies, applied dates, and interview statuses.
- **Interviews Governance**: Oversight of scheduled interview dates, times, round types, status, and candidate selection outcomes.

### 2. 🎓 Candidate / Applicant Panel (`ROLE_USER`)
- **Strict Mandatory Registration**: Validated form ensuring all fields (Full Name, Unique Username, Valid Email, 10-digit Mobile Number, Resume Upload [PDF/DOC/DOCX], Experience, Gender, Age, Location, Password, Password Confirmation) are filled before account creation.
- **Candidate Dashboard**: Personalized welcome greeting and dynamic metric cards (Jobs Available, Jobs Applied, Applications Pending, Shortlisted, Interviews Scheduled, Rejected).
- **Interactive Job Search & Filters**: Search jobs by keywords, filter by top hiring hubs (**Nagpur**, **Pune**, **Hyderabad**, **Mumbai**), experience, job types (Full Time, Part Time, Contract, Internship, Remote), and company.
- **One-Click Application & Duplicate Prevention**: Complete job details modal with applicant preview; prevents duplicate applications for the same job.
- **My Applications Tracker**: Live status updates across `PENDING`, `SHORTLISTED`, `SELECTED`, and `REJECTED` with recruiter feedback.
- **Interviews Section**: Immediate visibility into scheduled technical and HR rounds, including date, time, interview type, and meeting links.
- **Profile & Resume Management**: Edit personal details, change password, and replace active resumes.

### 3. 🏢 HR / Recruiter Panel (`ROLE_HR`)
- **HR Dashboard**: Vacancy statistics, active postings, candidate count, total applications, shortlisted count, scheduled interviews, and hires.
- **Job Requisition Management**: Full job publishing workflow (Job Title, Company, Description, Skills, Experience, Qualification, Location, Salary/CTC, Job Type, Openings, Application Deadline, Active/Closed status).
- **Candidate Pool**: Inspect candidate profiles, experience levels, view/download resumes, and update candidate hiring stages.
- **Applications Processing**: Job-wise application counters, candidate review, and immediate actions to Shortlist, Reject, or Select candidates.
- **Automated Interview Scheduler**: Schedule interview dates, times, round types (Online Technical, HR, Managerial, In-person), and video meeting links with instant notification to the candidate.

---

## 🛠️ Technology Stack

| Layer | Technologies |
|---|---|
| **Backend** | Java 21, Spring Boot 3.3.4, Spring MVC, Spring Data JPA, Hibernate ORM |
| **Security** | Spring Security 6 with BCrypt Password Hashing, CSRF protection, and Role-Based Authorization |
| **Frontend** | Thymeleaf 3, HTML5, CSS3, JavaScript (ES6+), Bootstrap 5.3.3, FontAwesome 6.5.1 |
| **Design System** | Custom modern palette, Plus Jakarta Sans typography, sleek card shadows, and responsive sidebar navigation |
| **Database** | MySQL 8.0 (with zero-friction H2 file fallback for instant demonstration) |
| **Build Tool** | Apache Maven 3.9+ |

---

## 🔑 Pre-Configured Demo Credentials

The database is preloaded with sample companies and positions across **Nagpur**, **Pune**, **Hyderabad**, and **Mumbai**:

| Role | Username / Email | Password | Details |
|---|---|---|---|
| **Admin** | `admin@ats.com` (or `admin`) | `admin123` | System Administrator with full access |
| **HR Recruiter** | `hr.techcorp@ats.com` (or `pooja_hr`) | `hr123` | ABC Technologies, Pune |
| **HR Recruiter** | `hr.innovate@ats.com` (or `vikram_hr`) | `hr123` | Innovate Global, Hyderabad |
| **HR Recruiter** | `hr.apex@ats.com` (or `anjali_hr`) | `hr123` | Apex Digital Systems, Nagpur |
| **HR Recruiter** | `hr.metro@ats.com` (or `suresh_hr`) | `hr123` | MetroCloud Technologies, Mumbai |
| **Candidate** | `rahul.sharma@example.com` (or `rahul_s`) | `user123` | Java Developer Applicant, Nagpur (3.5 Yrs) |
| **Candidate** | `priya.patil@example.com` (or `priya_p`) | `user123` | Frontend Engineer, Pune (2.0 Yrs) |
| **Candidate** | `amit.verma@example.com` (or `amit_v`) | `user123` | Python / AI Applicant, Hyderabad (4.0 Yrs) |
| **Candidate** | `sneha.deshmukh@example.com` (or `sneha_d`) | `user123` | Data Analyst, Mumbai (1.5 Yrs) |

*(Quick one-click login buttons are also provided on the login page for effortless evaluation.)*

---

## 🚀 Running the Application

### Option A: Standard Run (Out-of-the-Box)
The application is pre-configured with the `dev` profile using file-backed persistent storage in `./data/atsdb.mv.db`:
```bash
mvn spring-boot:run
```
Access the application in your browser at:
**[http://localhost:8080/](http://localhost:8080/)**

### Option B: Running with Local MySQL 8.0
1. Ensure your local MySQL server is running.
2. Import the database script:
   ```bash
   mysql -u root -p < recruitment_ats_db.sql
   ```
3. Set your MySQL credentials in `src/main/resources/application-mysql.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/recruitment_ats_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=YOUR_PASSWORD
   ```
4. Run with the MySQL profile:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=mysql
   ```

---

## 📁 Project Architecture

```
src/main/java/com/ats/recruitment/
├── config/              # DataInitializer (sample seed runner)
├── controller/          # AuthController, UserController, HRController, AdminController, ResumeController, ApiController
├── dto/                 # Form DTOs with mandatory validations & dashboard stats
├── entity/              # User, HR, Admin, Job, Application, Interview
├── exception/           # GlobalExceptionHandler, ResourceNotFoundException, DuplicateResourceException
├── repository/         # UserRepository, HRRepository, JobRepository, ApplicationRepository, InterviewRepository
├── security/            # SecurityConfig, CustomUserDetails, CustomUserDetailsService, CustomAuthenticationSuccessHandler
├── service/             # UserService, HRService, JobService, ApplicationService, InterviewService, FileStorageService
└── RecruitmentAtsApplication.java

src/main/resources/
├── templates/
│   ├── fragments/       # header, footer, navbar, sidebar, alerts
│   ├── admin/           # dashboard, users, user-edit, hr-list, hr-edit, jobs, job-edit, applications, interviews
│   ├── hr/              # dashboard, profile, jobs, job-create, job-edit, candidates, applications, interviews, schedule-interview
│   ├── user/            # dashboard, profile, jobs, job-details, applications, interviews
│   ├── index.html       # Public recruitment landing page
│   ├── login.html       # Role-based login page with test helpers
│   ├── register-user.html # Candidate registration with mandatory validation
│   └── register-hr.html # HR registration
├── static/
│   ├── css/custom.css   # Custom UI design system
│   └── js/custom.js     # Interactivity & file inputs
├── application.properties
├── application-dev.properties
└── application-mysql.properties
```
