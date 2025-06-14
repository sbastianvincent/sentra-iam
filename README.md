# 🛡️ Sentra IAM

[![BSD 3-Clause License](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](https://opensource.org/licenses/BSD-3-Clause)
![Build Status](https://github.com/sbastianvincent/sentra-iam/actions/workflows/build.yml/badge.svg)
![Checkstyle](https://github.com/sbastianvincent/sentra-iam/actions/workflows/checkstyle.yml/badge.svg)
[![codecov](https://codecov.io/gh/sbastianvincent/sentra-iam/branch/master/graph/badge.svg)](https://codecov.io/gh/sbastianvincent/sentra-iam)

**Sentra IAM** is the Identity and Access Management (IAM) service within the Sentra ecosystem. It handles user authentication, authorization, role-based access control, and integration with service-to-service security.

SentraIAM	&quot;Sentra&quot; (central in Bahasa) + IAM

---

## 📦 Features

- 🔐 User authentication (JWT-based)
- 🧑‍🤝‍🧑 Role-based access control (RBAC)
- 🗝️ OAuth2 token issuance and validation
- 🌐 Service-to-service trust and identity verification
- 📁 PKI & certificate-based identity support (optional)
- 🧪 Testable via Spring Boot profiles and mock integrations

---

## 🧰 Tech Stack

- Java 17
- Spring Security
- Spring Data JPA
- PostgreSQL
- Spring Cloud (APIGateway + Eureka Discovery)
- PKI or custom CA for internal identity
