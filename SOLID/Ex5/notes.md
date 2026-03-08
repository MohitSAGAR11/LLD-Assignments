The Problem: Unpredictable Subclasses Originally, the subclasses were breaking the rules of the parent class. The PdfExporter would crash if the text was too long, even though the base Exporter implied it could handle any request. JsonExporter and CsvExporter handled nulls and special characters inconsistently meaning the caller had to use try-catch blocks just to be safe.

Our Approach

First of all, we will make a clear base contract like i have moved null check and empty string handling to the final export() method in the base class. Now, no subclass has to worry about null pointers—they are guaranteed a clean request We will make sure that all these exportes behave in a same way for request so if we want to change the exporter in future we dont have to check for anything.

Example Lets Suppose we have to add a new Exporter(ExcelExporter) then in the old code, we can have different rules for character limits or null and might use try catch for this in main file hence it will behave differently then other exporters In the new way we will implement the Exporter in this class also and now our ExcelExporter will follow the same logic as other exporters for limits or null check, no we can swap between these exporter anytime without checking.
