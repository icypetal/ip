
# AI Tools Record

## Tools Used
Gemini 3 Pro (High) in Antigravity IDE in planning mode

## Level-0
- Required precise prompting to stay focused to the task and follow the git commands (tagging and pushing to remote)
- Follow-up prompts required to fulfill tasks.
- Overall did not save much time

## Level-1
- Was able to do it without errors and saved much time

## Level-2
- Using rules are very helpful to ensure that the AI remembers and follows the required instructions

## Level-3
- Was able to do it without errors and saved much time

## Level-4
- Was able to do it first try

## A-TextUiTesting
- Faced minor issues with the filepaths, but was able to rectify itself

## Level-5
- It copied the error messages directly (even though the website said not to), needing a few follow up prompts to fix it

## Level-6
- Was able to do it first try

## A-Enums
- Was able to do it first try

## Level-7
- The agent was able to follow the Git standard correctly, and perform the branch and merging correctly, albeit requiring follow up prompts as it forgot to merge the branch back to master

- The agent suddenly could not detect the workspace rules in the previous session, requiring me to now set the workspace rules as global rules

## Level-8
- It was able to implement the dates and times in yyyy-mm-dd format and print in MMM dd yyyy

## A-MoreOOP
- Was able to add the required classes to perform the required functions on the first try.
- Randomly decided to add the save file data/agy.txt to be tracked by git, requiring a follow-up prompt to rectify it

## A-Packages
- It had some issues moving files to proper directories as it assumed I was on Linux instead of Windows
- Took much longer to rectify than expected

## A-Gradle
- It seems that the Agent is becoming less intelligent as more tokens are being used (as I am reusing the same session)
- It tried to add internal files to the git repo, such as the implementation_plan.md
- It also did not follow the instructions as it tried to merge add-gradle-support to a separate branch first then to master, instead of directly to master.

## A-JUnit
- Faced some troubles with the agent running the tests, as it had some issues with Gradle's resource locks leading to errors, even though the tests itself were fine.

## A-Jar
- Generated the jar file successfully using gradle

## A-JavaDoc
- Added comments without issues

## A-CodingStandard
- No issues

## Level-9
- Logic was implemented without issue
- Managed to automatically resolve merge conflicts without human intervention
