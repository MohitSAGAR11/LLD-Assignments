The Problem -> Originally, the OnboardingService was a monolithic method that handled five distinct tasks: parsing raw strings, validating business rules, generating IDs, persisting data, and formatting console output. This made the code rigid and fragile.

Our Approach: Decoupling To solve this, we applied the Single Responsibility Principle (SRP) by breaking the Main Method into specialized classes:

InputParser: Handles the extraction of data from the raw semicolon-delimited string into a structured StudentData object. StudentValidator: Isolates the business rules of email format, program types from the rest of the flow. StudentRepository (Interface): Introduced an abstraction for data storage. By making OnboardingService depend on the interface rather than FakeDb, we satisfied the Dependency Inversion Principle (DIP). OnBoardingPrinter: Moved all System.out.println calls here. The OnboardingService now acts as a Coordinator. It doesn't know how to parse or how to save, it simply asks its helper components to perform their specific tasks in order.

Lets take an example, Suppose we have to add a new validation rule, then in the existing code we will have to search for the if else strings in the OnbaordingService but with the updated code, we can directly go to StudentValidator and add a new rule without changing other code hence preserving srp.
