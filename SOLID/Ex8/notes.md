The Problem: The Fat Interface Originally, the system used a single ClubAdminTools interface that bundled financial, minute-taking, and event-planning operations together. This forced the TreasurerTool to include irrelevant methods like addMinutes() and the SecretaryTool to include dummy logic for financial roles. This design led to fat classes filled with no-op or dummy implementations that were misleading and difficult to maintain.

Our Approach: Role-Based Interface Segregation To solve this, we split the responsibilities into three distinct, specialized interfaces:

FinanceTools: Contains only methods related to club finances (addIncome, addExpense). MinutesTools: Dedicated strictly to recording meeting minutes (addMinutes). PlanningTools: Focused solely on event management (createEvent, getEventsCount).

By having SecretaryTool only implement MinutesTools and TreasurerTool only implement FinanceTools, EventLeadTool only implementing Planning Tool , we eliminated all irrelevant method implementations.

Example Imagine we want to add a person who only handles SocialMedia for the club. The Old Way: We would have to force them to use the ClubAdminTools interface. Because that interface has everything, your new SocialMediaLead would be forced to write dummy code for the finance and meeting minutes just to get their code to compile. Hence we will have a messy class full of code that says // irrelevant or return 0. The New Way: You simply create a new, tiny interface called SocialMediaTools with one method: postToSocialMedia(). The Lead only implements SocialMediaTools. They don't see the finance rools , or any other irrelevant details they don't see the meeting minutes, and they don't have any dummy code.
