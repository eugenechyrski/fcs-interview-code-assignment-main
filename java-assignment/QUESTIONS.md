# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
```
I would refactor the logic to follow the approach used for the warehouse endpoint:
Maintain a clean separation of concerns to improve testability for both unit tests and end-to-end integration tests.
Structure the code consistently across endpoints to help new developers onboard more easily.
Avoid making REST methods transactional; transactions should reside in the business logic layer.
For example, the Product resource currently mixes validation and business logic. 
As business logic grows, it will become harder to unit test. Refactoring it into a dedicated service layer will improve maintainability and testability.
```
----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
```
Schema-first approach:

Pros: Centralized API contract, automatic generation of models/beans, useful for large enterprise projects with separate frontend and backend teams.

Cons: Generated code can be rigid, making custom behavior (e.g., returning a 201 status for createANewWarehouseUnit) harder to implement.

Code-first approach:

Pros: Keeps documentation close to the code, avoids code generation, suitable for smaller teams or single-developer end-to-end feature delivery.

Cons: Annotations can be bulky, and maintaining consistency across a large team can be harder.

For large projects with multiple teams, I prefer schema-first for consistency and automation. For smaller projects or rapid development by a single developer, code-first with OpenAPI annotations is also acceptable. 

```
----
3. Given the need to balance thorough testing with time and resource constraints, how would you prioritize and implement tests for this project? Which types of tests would you focus on, and how would you ensure test coverage remains effective over time?

**Answer:**
```
i would use frameworks like cucumber  and focus on integration testing making generic tests for each endpoint and allow to define new test cases and expected behaviour in cucumber feature files this will allow to rapidly add new testcases for existent functionality without touching code (if implemented correctly). Also produced reports are nice to look at.
  

```