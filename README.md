
# Project Task Sequencing and Planning

An algorithm to sequence and plan the tasks of a project based on the provided constraints.

- Sequencing refers to the act of ordering Epics / Stories / Tasks in the order stipulated by the business rules
- Planning refers to the act of assigning a specific Task to a specific Person on a specific Date

## Input

There are 2 data sets available to you for use in this Algorithm. Both these data sets will be in CSV files, and will have the following structure.

### Project Estimation

- A project contains many Epics

- An Epic has many Stories

- A Story has 3 Tasks (Backend, Frontend, QA)


| Epic | Story ID | Backend Task Effort (days) | Frontend Task Effort (days)| QA Task Effort (days)|
| ---- |----------| ---------------------------|----------------------------|----------------------|
| Home Page | Hero Banner| 1 | 2 | 1 |
| Home Page | Featured News| 1 | 0.5 | 0.5 |
| Contact Us Page | Contact Us Form| 1 | 0.5 | 0.5 |
| Contact Us Page | Address and Map Section| 0.5 | 1.5 | 1.5 |

### Staffing Sheet

|Person|Skill|25-Aug|26-Aug|27-Aug|28-Aug|29-Aug|30-Aug|31-Aug|1-Sep|2-Sep|3-Sep|
|------|-----|------|------|------|------|------|------|------|-----|-----|-----|
|Name 1| BE  | 1|1|0|1|1|1|1|1|1|1|
|Name 2| FE | 1|1|0|1|1|1|1|1|1|1|
|Name 3| QA  | 0|0|0|1|1|0.5|0.5|0.5|0.5|0.5|



The staffing sheet contains information 
- List of people who are aligned to work on that project
- Their primary skill
- Percentage allocation of that person for each day of the project duration. For eg.
  - Public Holidays, Vacations and Sick / Casual Leaves are depicted as “0”
  - “1” represents availability at 100% (8 hours) on that day
  - “0.5” represents availability at 50% (4 hours) on that day




### Business / Functional Requirements

|S.No.|Business Requirements|
|-----|---------------------|
|1|Epics should be sequenced and planned in the same order in which they appear in the Input Data|
|2|Stories within an Epic should also be sequenced and planned in the same order in which they appear in the Input Data |
|3|Tasks within a Story should be sequenced and planned as follows: Backend tasks, then Frontend tasks, then QA tasks|
|4|Nothing should be planned for a Person when their availability is “0”. In general, every person should be given exact time to finish a task|
|5|A Task can be spread across multiple days, but it should not be spread across multiple people|
|6|More than one Task can be assigned to a person on a given day|
|7|There can be more than 1 person of a particular skill|



### Non-Functional Requirements

The number of working hours which correspond to 100% allocation should be configurable. Its default value should be “8” hours, however there should be flexibility to change this for various reasons 
- Some teams estimate based on 8 hours, while others estimate based on 7 hours
- Teams may want to 
    - Increase it when project is lagging behind and needs more hours per day from the team members
    - Decrease it, when the project has elaborate client-mandated communication (meeting) overheads.|


## Output


The expected output is a Low Level Plan / Daily Plan / Sprint Plan.

|Person|25-Aug|26-Aug|27-Aug|28-Aug|29-Aug|30-Aug|31-Aug|1-Sep|2-Sep|3-Sep|
|------|------|------|------|------|------|------|------|------|------|------|
|Name 1|Hero Banner|Featured News|Not Available|Contact Us Form|Address and Map Section|8 Hr Spare|8 Hr Spare|8 Hr Spare|8 Hr Spare|8 Hr Spare|
|Name 2|8Hr Spare|Hero Banner|Not Available|Hero Banner|Featured News    Contact Us Form|Address and Map Section|Address and Map Section. 4 Hr Spare|8 Hr Spare|8 Hr Spare|8 Hr Spare|
|Name 3|Not Available|Not Available|Not Available|Not Available|Hero Banner|Hero Banner|Featured News|Contact Us Form|Address and Map Section|Address and Map Section|



This output should be exported as a CSV, so that it can be opened in Excel, the primary tool for project managers.

### Project Best Practices 

|S.No |Best Practices and standards|
|----|------|
|1|All code should be saved on a Github.com repository, and everyone should have access to the same master copy of code.|
|2|All the developers should check-in their code into GIT on a daily basis|
|3|Everyone who pushes code into GIT must ensure they do not leave the project code in an uncompilable / unstable state or overwrite other people’s changes|
