# Vagabond Quarkus Common API

org.vagabond.quarkus > auth-api

## api-engine

- auth
- crud (abstract Entity / Repository / Service / Resource / Response)
- exception (MetierException / TechnicalException / Handler)
- Gateway (Filter : read X-User- headers)
- Http (Component)
- Mapper (Utils)
- User (CurrentUser Dto)

## common-core

- auth (Interceptor / Validate @AuthRole with @AuthSecure)
- email (send email)
- file (Upload / Download)
- news
- notification
- stripe

## common-api

- Resource for all

## common-kafka

- topics
  - email
  - notification
