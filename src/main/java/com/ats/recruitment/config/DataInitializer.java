package com.ats.recruitment.config;

import com.ats.recruitment.entity.*;
import com.ats.recruitment.repository.*;
import com.ats.recruitment.service.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final HRRepository hrRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    public DataInitializer(AdminRepository adminRepository,
                           HRRepository hrRepository,
                           UserRepository userRepository,
                           JobRepository jobRepository,
                           ApplicationRepository applicationRepository,
                           InterviewRepository interviewRepository,
                           PasswordEncoder passwordEncoder,
                           FileStorageService fileStorageService) {
        this.adminRepository = adminRepository;
        this.hrRepository = hrRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public void run(String... args) {
        if (adminRepository.count() > 0) {
            return; // Data already initialized
        }

        System.out.println(">>> Initializing Recruitment and Applicant Tracking System sample data...");

        // 1. Create sample resume files
        String samplePdfContent = "%PDF-1.4\n1 0 obj\n<< /Title (Candidate Resume) /Author (ATS System) >>\nendobj\n" +
                "2 0 obj\n<< /Type /Catalog /Pages 3 0 R >>\nendobj\n" +
                "3 0 obj\n<< /Type /Pages /Kids [4 0 R] /Count 1 >>\nendobj\n" +
                "4 0 obj\n<< /Type /Page /Parent 3 0 R /MediaBox [0 0 612 792] /Contents 5 0 R >>\nendobj\n" +
                "5 0 obj\n<< /Length 55 >>\nstream\nBT /F1 12 Tf 70 700 Td (Candidate Professional Resume - ATS Profile) Tj ET\nendstream\nendobj\nxref\n0 6\n0000000000 65535 f \n" +
                "trailer\n<< /Root 2 0 R /Size 6 >>\nstartxref\n380\n%%EOF";

        byte[] pdfBytes = samplePdfContent.getBytes(StandardCharsets.UTF_8);
        fileStorageService.storeSampleResume("sample_resume_rahul.pdf", pdfBytes);
        fileStorageService.storeSampleResume("sample_resume_priya.pdf", pdfBytes);
        fileStorageService.storeSampleResume("sample_resume_amit.pdf", pdfBytes);
        fileStorageService.storeSampleResume("sample_resume_sneha.pdf", pdfBytes);

        // 2. Create Admin
        Admin admin = new Admin();
        admin.setName("Super Admin");
        admin.setUsername("admin");
        admin.setEmail("admin@ats.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole("ROLE_ADMIN");
        adminRepository.save(admin);

        // 3. Create HR Accounts
        HR hrPune = new HR("Pooja Kulkarni", "pooja_hr", "hr.techcorp@ats.com", "9876543210",
                "ABC Technologies", "Female", "Pune", passwordEncoder.encode("hr123"));
        hrRepository.save(hrPune);

        HR hrHyd = new HR("Vikram Reddy", "vikram_hr", "hr.innovate@ats.com", "9823456789",
                "Innovate Global", "Male", "Hyderabad", passwordEncoder.encode("hr123"));
        hrRepository.save(hrHyd);

        HR hrNagpur = new HR("Anjali Sharma", "anjali_hr", "hr.apex@ats.com", "9765432109",
                "Apex Digital Systems", "Female", "Nagpur", passwordEncoder.encode("hr123"));
        hrRepository.save(hrNagpur);

        HR hrMumbai = new HR("Suresh Mehta", "suresh_hr", "hr.metro@ats.com", "9812345678",
                "MetroCloud Technologies", "Male", "Mumbai", passwordEncoder.encode("hr123"));
        hrRepository.save(hrMumbai);

        // 4. Create Users (Candidates)
        User userRahul = new User("Rahul Sharma", "rahul_s", "rahul.sharma@example.com", "9876501234",
                "sample_resume_rahul.pdf", 3.5, "Male", 26, "Nagpur", passwordEncoder.encode("user123"));
        userRahul.setResumeOriginalName("Rahul_Sharma_Resume.pdf");
        userRepository.save(userRahul);

        User userPriya = new User("Priya Patil", "priya_p", "priya.patil@example.com", "9876505678",
                "sample_resume_priya.pdf", 2.0, "Female", 24, "Pune", passwordEncoder.encode("user123"));
        userPriya.setResumeOriginalName("Priya_Patil_Resume.pdf");
        userRepository.save(userPriya);

        User userAmit = new User("Amit Verma", "amit_v", "amit.verma@example.com", "9876509012",
                "sample_resume_amit.pdf", 4.0, "Male", 28, "Hyderabad", passwordEncoder.encode("user123"));
        userAmit.setResumeOriginalName("Amit_Verma_Resume.pdf");
        userRepository.save(userAmit);

        User userSneha = new User("Sneha Deshmukh", "sneha_d", "sneha.deshmukh@example.com", "9876503456",
                "sample_resume_sneha.pdf", 1.5, "Female", 23, "Mumbai", passwordEncoder.encode("user123"));
        userSneha.setResumeOriginalName("Sneha_Deshmukh_Resume.pdf");
        userRepository.save(userSneha);

        // 5. Create Jobs across Nagpur, Pune, Hyderabad, Mumbai
        Job jobJavaPune = new Job("Java Developer", "ABC Technologies",
                "We are seeking an experienced Java Developer to develop scalable enterprise microservices using Spring Boot, Hibernate, and RESTful APIs.",
                "Java, Spring Boot, Microservices, MySQL, REST API, Docker",
                "2 - 5 Years", "B.Tech / B.E / MCA in Computer Science",
                "Pune", "₹ 8,00,000 - ₹ 14,00,000 P.A.", "Full Time", 4,
                LocalDate.now().plusDays(30), hrPune);
        jobRepository.save(jobJavaPune);

        Job jobPythonHyd = new Job("Python Developer", "Innovate Global",
                "Looking for a Python Developer proficient in Django/FastAPI and building data pipelines and backend cloud services.",
                "Python, Django, FastAPI, PostgreSQL, Redis, AWS",
                "3 - 6 Years", "B.Tech / MCA",
                "Hyderabad", "₹ 9,00,000 - ₹ 16,00,000 P.A.", "Full Time", 3,
                LocalDate.now().plusDays(25), hrHyd);
        jobRepository.save(jobPythonHyd);

        Job jobDataAnalystNagpur = new Job("Data Analyst", "Apex Digital Systems",
                "Apex Systems is hiring a Data Analyst to translate complex datasets into actionable business intelligence reports.",
                "SQL, Python, PowerBI, Tableau, Advanced Excel, Statistics",
                "1 - 3 Years", "B.Sc Statistics / B.Tech / MBA",
                "Nagpur", "₹ 5,50,000 - ₹ 9,00,000 P.A.", "Full Time", 2,
                LocalDate.now().plusDays(20), hrNagpur);
        jobRepository.save(jobDataAnalystNagpur);

        Job jobFrontendPune = new Job("Frontend Developer", "ABC Technologies",
                "Passionate Frontend Engineer required to build intuitive, responsive web applications with modern HTML5, CSS3, JavaScript, and React.",
                "HTML5, CSS3, JavaScript (ES6+), React, Bootstrap, Webpack",
                "1 - 4 Years", "Graduate in Computer Science or related",
                "Pune", "₹ 6,50,000 - ₹ 11,00,000 P.A.", "Full Time", 3,
                LocalDate.now().plusDays(40), hrPune);
        jobRepository.save(jobFrontendPune);

        Job jobBackendMumbai = new Job("Backend Developer", "MetroCloud Technologies",
                "MetroCloud is looking for a senior backend engineer to architect distributed cloud services and database systems.",
                "Java, Spring Boot, MySQL, Kafka, Redis, Kubernetes",
                "3 - 7 Years", "B.E / B.Tech Computer Engineering",
                "Mumbai", "₹ 12,00,000 - ₹ 20,00,000 P.A.", "Full Time", 2,
                LocalDate.now().plusDays(35), hrMumbai);
        jobRepository.save(jobBackendMumbai);

        Job jobAiMlHyd = new Job("AI/ML Engineer", "Innovate Global",
                "Build state-of-the-art predictive machine learning models, NLP pipelines, and computer vision systems.",
                "Python, PyTorch, TensorFlow, NLP, Scikit-learn, MLOps",
                "2 - 5 Years", "M.Tech / B.Tech in AI / CS",
                "Hyderabad", "₹ 14,00,000 - ₹ 22,00,000 P.A.", "Full Time", 2,
                LocalDate.now().plusDays(45), hrHyd);
        jobRepository.save(jobAiMlHyd);

        Job jobCloudNagpur = new Job("Cloud DevOps Engineer", "Apex Digital Systems",
                "Automate CI/CD pipelines, configure cloud infrastructure, and ensure 99.9% uptime for core enterprise services.",
                "Linux, Docker, Kubernetes, Jenkins, Terraform, AWS",
                "2 - 4 Years", "B.Tech / B.E",
                "Nagpur", "₹ 7,50,000 - ₹ 13,00,000 P.A.", "Full Time", 2,
                LocalDate.now().plusDays(28), hrNagpur);
        jobRepository.save(jobCloudNagpur);

        // 6. Create Applications
        // Rahul -> Java Pune (SHORTLISTED)
        Application appRahulJava = new Application(userRahul, jobJavaPune);
        appRahulJava.setStatus("SHORTLISTED");
        appRahulJava.setNotes("Impressive experience with Spring Boot microservices and REST APIs.");
        appRahulJava.setApplicationDate(LocalDateTime.now().minusDays(3));
        applicationRepository.save(appRahulJava);

        // Rahul -> Backend Mumbai (PENDING)
        Application appRahulBackend = new Application(userRahul, jobBackendMumbai);
        appRahulBackend.setStatus("PENDING");
        appRahulBackend.setApplicationDate(LocalDateTime.now().minusDays(1));
        applicationRepository.save(appRahulBackend);

        // Priya -> Frontend Pune (SELECTED)
        Application appPriyaFrontend = new Application(userPriya, jobFrontendPune);
        appPriyaFrontend.setStatus("SELECTED");
        appPriyaFrontend.setNotes("Top score in UI coding round and excellent culture fit.");
        appPriyaFrontend.setApplicationDate(LocalDateTime.now().minusDays(7));
        applicationRepository.save(appPriyaFrontend);

        // Amit -> Python Hyd (SHORTLISTED)
        Application appAmitPython = new Application(userAmit, jobPythonHyd);
        appAmitPython.setStatus("SHORTLISTED");
        appAmitPython.setNotes("Strong background in FastAPI and asynchronous processing.");
        appAmitPython.setApplicationDate(LocalDateTime.now().minusDays(4));
        applicationRepository.save(appAmitPython);

        // Amit -> AI/ML Hyd (PENDING)
        Application appAmitAi = new Application(userAmit, jobAiMlHyd);
        appAmitAi.setStatus("PENDING");
        appAmitAi.setApplicationDate(LocalDateTime.now().minusDays(2));
        applicationRepository.save(appAmitAi);

        // Sneha -> Data Analyst Nagpur (REJECTED)
        Application appSnehaAnalyst = new Application(userSneha, jobDataAnalystNagpur);
        appSnehaAnalyst.setStatus("REJECTED");
        appSnehaAnalyst.setNotes("Looking for candidates with more advanced PowerBI experience.");
        appSnehaAnalyst.setApplicationDate(LocalDateTime.now().minusDays(5));
        applicationRepository.save(appSnehaAnalyst);

        // 7. Create Interviews
        // Rahul's Java Interview
        Interview interviewRahul = new Interview(
                appRahulJava,
                userRahul,
                jobJavaPune,
                hrPune,
                LocalDate.now().plusDays(5),
                "11:00 AM",
                "Online Technical Round",
                "https://meet.google.com/ats-java-tech",
                "Discussion on Spring Boot architecture, transactions, and coding assessment."
        );
        interviewRahul.setStatus("SCHEDULED");
        interviewRepository.save(interviewRahul);

        // Amit's Python Interview
        Interview interviewAmit = new Interview(
                appAmitPython,
                userAmit,
                jobPythonHyd,
                hrHyd,
                LocalDate.now().plusDays(7),
                "02:30 PM",
                "Technical Coding & Architecture",
                "https://meet.google.com/ats-py-design",
                "Live coding round on algorithms and microservice design."
        );
        interviewAmit.setStatus("SCHEDULED");
        interviewRepository.save(interviewAmit);

        // Priya's Frontend Interview (Completed & Selected)
        Interview interviewPriya = new Interview(
                appPriyaFrontend,
                userPriya,
                jobFrontendPune,
                hrPune,
                LocalDate.now().minusDays(3),
                "10:00 AM",
                "Final Technical & HR Discussion",
                "TechCorp Campus, Tower B, Pune",
                "Candidate cleared all rounds with distinction."
        );
        interviewPriya.setStatus("SELECTED");
        interviewRepository.save(interviewPriya);

        System.out.println(">>> Sample data initialized successfully!");
        System.out.println(">>> Login credentials:");
        System.out.println("    Admin: admin@ats.com / admin123 (or 'admin' / 'admin123')");
        System.out.println("    HR:    hr.techcorp@ats.com / hr123");
        System.out.println("    User:  rahul.sharma@example.com / user123 (or 'rahul_s' / 'user123')");
    }
}
