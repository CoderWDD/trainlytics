# Design Blueprint — Trainlytics

## Purpose

This document is the UI/UX design input for Trainlytics before implementation planning.

It defines:

- What surfaces the Android app needs
- Which screens matter most in V1
- Which components must exist to support those screens
- What states and interactions the design must cover

This is not yet a final visual system. It is the structural design blueprint that a UI/UX pass should work from.

## Product Context

- **What this is:** a local-first Android app for recording bodyweight, meals, training, strength, and a small set of recovery or activity metrics during a cut
- **Who it is for:** one primary user, the app owner, who trains regularly and wants disciplined self-tracking
- **Project type:** Android productivity + quantified-self fitness tracker
- **Core product promise:** make daily logging sustainable, then make weekly progress easy to read

## Design Intent

The product should feel:

- Disciplined
- Calm
- Capable
- Personal
- Fast to use in real life

The product should avoid feeling:

- Gamified like a calorie app
- Clinical like a medical dashboard
- Loud like a bodybuilding social app
- Generic like a CRUD admin panel

## UX Principles

1. **Today is the center**
   - The user should understand today's status in a few seconds.

2. **Fast capture beats perfect detail**
   - The default path should be quick. Detail should always be available, but never mandatory.

3. **Progressive disclosure**
   - Meals, sets, and extra metrics should expand only when needed.

4. **Templates must be visible in the flow**
   - Templates should not live only in a management screen.

5. **Analysis is separate from entry**
   - Logging surfaces should stay focused on capture.

6. **History must remain trustworthy**
   - Editing templates later must not change what was logged in the past.

7. **Android-native interaction patterns**
   - Prefer bottom sheets, direct manipulation, inline editing, and one-handed reachability.

## Platform Interaction Guidance

The app is Android-first, so the default interaction patterns should be:

- Bottom navigation for top-level destinations
- Floating action button or equivalent global Quick Add entry
- Modal bottom sheets for fast-entry tasks
- Full-screen flow for active workout logging
- Inline save or auto-save where safe
- Numeric-first input for weight, macros, and sets
- Sticky primary actions only where needed

The app should be comfortable during:

- Morning weigh-in
- Meal-time logging
- In-gym use with short attention windows
- End-of-week review

## Design Scope For V1

### Must Design Now

- App shell
- Today
- Quick Add sheet
- Body check-in entry
- Meal entry
- Active workout
- History
- Insights overview
- Weekly review
- Templates home

### Supporting Screens To Design After Core Flows

- First-run setup
- Historical day detail
- Trend detail
- Form template editor
- Training template editor
- Meal template editor
- Custom metrics manager
- Settings and export

## App Map

### Primary Navigation

- `Today`
- `History`
- `Insights`
- `Templates`

### Global Entry

- `Quick Add`

### Secondary Screens

- First-run setup
- Historical day detail
- Active workout
- Trend detail
- Weekly review
- Form template editor
- Training template editor
- Meal template editor
- Custom metrics manager
- Settings
- Export

## Surface Inventory

| ID | Surface | Type | Priority | Purpose |
| --- | --- | --- | --- | --- |
| 1 | App Shell | Global frame | P0 | Holds nav, app bars, global quick add, and consistent layout rules |
| 2 | First-Run Setup | Screen flow | P1 | Sets defaults such as phase, default form template, meal slots, and optional custom metrics |
| 3 | Today | Primary screen | P0 | Main daily logging and status overview |
| 4 | Quick Add | Global bottom sheet | P0 | Fast capture from anywhere |
| 5 | Body Check-In Entry | Bottom sheet or embedded card editor | P0 | Record weight, body fat, waist, and selected recovery metrics |
| 6 | Meal Entry | Bottom sheet | P0 | Record one meal quickly with macro totals |
| 7 | Meal Detail | Expanded sheet state | P0 | Add notes and optional food items under a meal |
| 8 | Start Workout | Bottom sheet | P0 | Start from a template or empty workout |
| 9 | Active Workout | Full screen | P0 | Log exercises and sets during training |
| 10 | Workout Finish Summary | Bottom sheet or end state | P1 | Review session totals before closing |
| 11 | History | Primary screen | P0 | Browse past days through calendar and timeline |
| 12 | Historical Day Detail | Reused Today variant | P1 | View and edit a past day using the same structure as Today |
| 13 | Insights Overview | Primary screen | P0 | Read high-level trends and recent performance |
| 14 | Trend Detail | Secondary screen | P1 | Inspect one metric in more detail |
| 15 | Weekly Review | Secondary screen | P0 | Summarize one week across bodyweight, intake, training, and notes |
| 16 | Templates Home | Primary screen | P0 | Enter form and content template management |
| 17 | Form Template Editor | Secondary screen | P1 | Control daily structure and field visibility |
| 18 | Training Template Editor | Secondary screen | P1 | Define reusable workouts and planned sets |
| 19 | Meal Template Editor | Secondary screen | P1 | Define reusable meal presets |
| 20 | Custom Metrics Manager | Secondary screen | P1 | Add small auxiliary metrics like steps or sleep hours |
| 21 | Settings | Secondary screen | P1 | Preferences, defaults, data handling, and app behavior |
| 22 | Export | Secondary screen | P1 | Export CSV and JSON |

## Detailed Screen Notes

### 1. App Shell

**Purpose**

- Create a stable frame for the whole app
- Keep primary navigation obvious
- Keep Quick Add reachable from anywhere

**Required areas**

- Top app bar
- Bottom nav
- Quick Add entry
- Overflow entry for settings

**Key design decisions**

- Bottom nav should never exceed four top-level items
- Settings should not occupy a main tab
- Quick Add should feel global, not tied to one tab

### 2. First-Run Setup

**Purpose**

- Avoid a blank first day
- Establish just enough defaults so the app feels ready immediately

**Setup items**

- Phase label or goal context
- Default form template
- Default meal slots
- Optional custom metrics

**States**

- Minimal setup path
- Skip custom metrics path
- Confirm ready state

### 3. Today

**Purpose**

- Show today's logging status in one place
- Let the user enter and review the full day

**Sections**

- Top summary
- Body status
- Meals
- Training
- Daily notes

**Primary actions**

- Record body check-in
- Add or edit a meal
- Start or continue workout
- Add notes
- Change today's form template

**Critical states**

- Brand-new day
- Partial day
- Fully logged day
- Training day
- Rest day

**UX notes**

- This screen should not feel like a giant form
- Section summaries should be visible even when content is collapsed
- Completion should be suggestive, not punitive

### 4. Quick Add

**Purpose**

- Shorten the path to high-frequency actions

**Actions**

- Record weight
- Add meal
- Start workout
- Add recovery or activity metric

**UX notes**

- Keep this sheet short and confident
- Avoid turning it into a second navigation menu

### 5. Body Check-In Entry

**Purpose**

- Support quick morning or midday body logging

**Fields**

- Weight
- Body fat
- Waist
- Selected recovery metrics

**States**

- Weight only
- Weight plus optional fields
- Validation errors for impossible values

**UX notes**

- Weight must be the fastest field to enter
- Optional fields should not visually compete with weight

### 6. Meal Entry

**Purpose**

- Make meal capture possible in under a minute

**Fields**

- Meal slot
- Time
- Calories
- Protein
- Optional carbs
- Optional fat
- Notes
- Apply meal template

**States**

- New meal
- Existing meal edit
- Meal from template

**UX notes**

- Default to totals, not food breakdown
- Numeric keypad and fast focus order matter a lot here

### 7. Meal Detail

**Purpose**

- Add optional detail without slowing down the default path

**Elements**

- Notes
- Food item list
- Add food item row

**States**

- Totals only
- Totals plus several food items
- Empty detail state

**UX notes**

- Food items should feel optional and additive
- The design should not imply failure if the user leaves detail empty

### 8. Start Workout

**Purpose**

- Let the user start training with minimal ceremony

**Options**

- Start from training template
- Start empty workout
- Continue unfinished workout if one exists

**UX notes**

- Template choice should be the primary path
- Empty workout should still be available for irregular days

### 9. Active Workout

**Purpose**

- Support real-time training logging without friction

**Main regions**

- Session header
- Exercise list
- Set rows
- Add exercise
- Finish workout action

**Primary actions**

- Edit weight and reps per set
- Add or remove sets
- Add exercise
- Reorder exercises if needed
- Mark workout complete

**Critical states**

- Fresh workout from template
- Empty workout
- In-progress workout
- Completed workout

**UX notes**

- This is the most interaction-heavy screen in the app
- It should optimize for speed, not decoration
- Set editing must be visually dense but still clear

### 10. Workout Finish Summary

**Purpose**

- Provide closure and a confidence check before exit

**Summary content**

- Session duration
- Total volume
- Top set highlights
- Notes

**UX notes**

- Keep this short
- It is a review surface, not a report page

### 11. History

**Purpose**

- Let the user browse, compare, and backfill days

**Main regions**

- Calendar strip or month view
- Timeline list
- Filters if needed later

**Critical states**

- Empty history
- Sparse history
- Fully populated recent week
- Day with missing sections

**UX notes**

- Missing or partial days should be discoverable
- History should feel calm, not like an inbox of failures

### 12. Historical Day Detail

**Purpose**

- Reuse the Today structure for past dates

**Design rule**

- Reuse the Today layout wherever possible
- Add a clear date context banner so the user knows they are not on the current day

### 13. Insights Overview

**Purpose**

- Show whether the cut is progressing normally

**Modules**

- Weight trend
- Calories trend
- Protein trend
- Training trend
- Core lift trend
- Optional activity or recovery trend

**UX notes**

- Show smoothed trends where needed
- Avoid overwhelming the user with too many charts at once

### 14. Trend Detail

**Purpose**

- Let the user inspect one metric in detail

**Possible content**

- Larger chart
- Time range selector
- Reference lines such as target or weekly average
- Related note callouts

### 15. Weekly Review

**Purpose**

- Summarize one week's signal in a stable format

**Sections**

- Bodyweight change
- Intake averages
- Training count and volume
- Strength status
- Recovery or activity snapshot
- Weekly notes

**UX notes**

- This screen should read like a disciplined review, not a flashy dashboard

### 16. Templates Home

**Purpose**

- Separate template management from day-to-day capture

**Areas**

- Form templates
- Training templates
- Meal templates

**UX notes**

- Make it obvious which templates affect structure and which affect content

### 17. Form Template Editor

**Purpose**

- Control the structure of Today for a given day type

**Editable items**

- Section visibility
- Ordering
- Default expansion
- Included custom metrics
- Default values

### 18. Training Template Editor

**Purpose**

- Define reusable workouts

**Editable items**

- Workout name
- Exercise list
- Planned sets
- Notes

### 19. Meal Template Editor

**Purpose**

- Define reusable meal presets

**Editable items**

- Meal name
- Default macros
- Optional food item defaults
- Notes

### 20. Custom Metrics Manager

**Purpose**

- Manage small auxiliary fields without turning the app into a form builder

**Editable items**

- Metric name
- Type
- Unit
- Category

### 21. Settings

**Purpose**

- Centralize behavior and preference changes

**Areas**

- Units
- Defaults
- Export access
- App behavior and date preferences

### 22. Export

**Purpose**

- Let the user safely export raw data

**Formats**

- CSV
- JSON

**UX notes**

- Export should feel reliable and transparent
- Show what is included before export starts

## Global Component Inventory

| Component | Used On | Variants Or States | Notes |
| --- | --- | --- | --- |
| Top App Bar | All top-level screens | standard, scrolled, contextual | Keeps title, date context, and overflow actions |
| Bottom Navigation | App shell | active tab, badge, disabled during modal flow | Only four primary destinations |
| Quick Add Trigger | App shell | idle, expanded, hidden during active workout | Global high-frequency action |
| Section Header | Today, Insights, Templates | with action, with summary, collapsible | Reused structural pattern |
| Status Chip | Today, History, Weekly Review | empty, partial, complete, rest day, training day | Communicates logging state |
| Metric Card | Today, Insights | compact, expanded | Reused for weight and auxiliary metrics |
| Numeric Input Row | Body, Meal, Set entry | focused, valid, invalid | Core input primitive |
| Meal Card | Today, History | empty, summary, expanded, from template | Shows meal-level state quickly |
| Macro Summary Strip | Meal surfaces | calories only, full macro set | Keeps nutrition information scan-friendly |
| Food Item Row | Meal detail | editable, collapsed, deleted | Secondary detail component |
| Template Chip | Meal, Workout, Templates | applied, suggested, historical source | Makes template usage visible |
| Workout Session Header | Active workout | in progress, paused, completed | Anchors the training flow |
| Exercise Card | Active workout | template-derived, custom, collapsed, expanded | Holds one exercise and its sets |
| Set Row | Active workout | planned, completed, edited, invalid | Highest-frequency repeated component |
| Add Row Button | Meal detail, Active workout | default, pressed | Reused inline expansion action |
| Timeline Row | History | complete, partial, empty day, selected | Helps compare days at a glance |
| Calendar Day Cell | History | empty, partial, complete, selected, today | Encodes coverage without clutter |
| Chart Card | Insights | small, expanded, loading, no-data | Unified chart framing |
| Weekly Stat Card | Weekly review | positive, neutral, warning | Used for concise review metrics |
| Template Card | Templates | form, meal, training, archived | Management surface building block |
| Empty State Panel | Many surfaces | no data, no templates, no history | Must feel encouraging, not accusatory |
| Inline Validation Message | Entry surfaces | warning, error | Keep copy short and numeric-specific |
| Snackbar / Toast | Global | saved, export complete, validation warning | Confirm low-risk actions without interrupting flow |
| Confirmation Dialog | Export, delete, discard | soft confirm, destructive confirm | Use sparingly |

## Cross-Screen State Model

These states must be designed consistently across surfaces.

### Logging States

- Empty
- Draft or partial
- Complete
- Historical
- Template-derived

### Data States

- No data yet
- Some data but missing sections
- Valid saved data
- Invalid field value
- Export ready

### System States

- Auto-saved
- Save failed
- Long-running export

Even though the app is local-first, the design still needs explicit failure states for save and export paths.

## Key User Flows To Design End-To-End

### Flow 1: Morning Check-In

1. Open Today
2. Tap body section
3. Enter weight
4. Optionally add body fat, waist, sleep, or other metrics
5. Return to Today with updated summary

### Flow 2: Quick Meal Capture

1. Trigger Quick Add
2. Choose Add Meal
3. Select meal slot
4. Enter calories and protein
5. Optionally add carbs, fat, notes, or template
6. Return to Today

### Flow 3: Template-Based Workout Logging

1. Start workout
2. Choose training template
3. Log sets during training
4. Adjust exercise content if reality differs from plan
5. Finish workout and review summary

### Flow 4: Weekly Review

1. Open Insights
2. Inspect top-level trends
3. Enter weekly review
4. Compare this week to last week
5. Read notes and context together with metrics

### Flow 5: Backfill A Missed Day

1. Open History
2. Select partial or empty day
3. Reuse Today-style layout for that date
4. Add missing meals, notes, or metrics

## P0 Wireframe Blueprints

This section translates the screen inventory into concrete mobile layout guidance for first-pass wireframes.

### Today Wireframe Blueprint

**Mobile layout order**

1. Top app bar
2. Daily summary block
3. Body status section
4. Meals section
5. Training section
6. Daily notes section
7. Bottom navigation

**Daily summary block should contain**

- Current date
- Active phase label
- A small completion overview
- A small entry point to switch today's form template

**Body status section should contain**

- Primary weight card
- Secondary body metrics row
- Auxiliary recovery or activity metric row
- One primary action to record or edit check-in

**Meals section should contain**

- One card per meal slot
- Summary state visible even when collapsed
- Inline action to add a missing meal
- Optional small template indicator if a meal came from a preset

**Training section should contain**

- Empty state with start-workout CTA
- Or active session summary if a workout exists
- If completed, show concise workout highlights instead of the full set table

**Daily notes section should contain**

- Short notes preview
- Expandable full editor

**Wireframe rules**

- Only one section should feel primary at a time
- The page should scan well in under five seconds
- Do not make every section the same height
- Use compact summaries first, detail second

### Quick Add Blueprint

**Form factor**

- Bottom sheet
- Opens from any primary screen

**Content**

- Sheet title
- Four high-frequency actions
- Optional recent action row only if it proves useful later

**Actions**

- Record weight
- Add meal
- Start workout
- Add recovery or activity metric

**Wireframe rules**

- This should read like a fast launcher, not a settings menu
- Actions should be immediately tappable with no explanatory copy overload

### Meal Entry Blueprint

**Form factor**

- Bottom sheet by default

**Layout**

1. Meal slot selector
2. Time row
3. Calories field
4. Protein field
5. Optional carb and fat row
6. Template entry point
7. Notes field
8. Expand-to-detail area for food items
9. Save action

**Wireframe rules**

- Calories and protein should appear before anything optional
- Food-item detail should be visually secondary
- Numeric entry flow must feel linear and quick

### Active Workout Blueprint

**Form factor**

- Full-screen dedicated flow

**Layout**

1. Session header with workout name, elapsed time, and overflow actions
2. Exercise list
3. Within each exercise card:
   - Exercise title row
   - Optional notes
   - Set table
   - Add set action
4. Add exercise action
5. Sticky finish-workout action

**Wireframe rules**

- The exercise list is the core of the screen, not the header
- Set rows need high information density
- Finish action must remain reachable without excessive scrolling
- Avoid decorative banners or oversized spacing in this flow

### History Blueprint

**Layout**

1. Top app bar
2. Calendar strip or month entry point
3. Timeline list of recent days
4. Day rows with completion and context summary

**Each day row should be able to show**

- Date
- Weight summary
- Meal completion or count
- Workout presence
- Partial or complete status

**Wireframe rules**

- The user should be able to identify incomplete days instantly
- Do not overload the row with tiny badges for every metric

### Insights Overview Blueprint

**Layout**

1. Top app bar
2. Summary strip with the most important recent signal
3. Weight chart card
4. Nutrition chart card
5. Training chart card
6. Optional activity or recovery card
7. Weekly review entry point

**Wireframe rules**

- Weight should be visually first
- Charts should be scrollable cards, not tiny sparklines with no context
- Weekly review should feel like the synthesis destination for this tab

### Weekly Review Blueprint

**Layout**

1. Week selector
2. Headline summary card
3. Bodyweight section
4. Intake section
5. Training section
6. Recovery or activity section
7. Notes section

**Wireframe rules**

- The page should feel structured like a review template
- Keep comparisons to last week obvious
- Avoid making every stat equally prominent

### Templates Home Blueprint

**Layout**

1. Top app bar
2. Form templates group
3. Training templates group
4. Meal templates group
5. Create template actions

**Wireframe rules**

- Clearly separate structure templates from content templates
- Use naming and grouping to reduce cognitive load

## Component Anatomy And Layout Rules

These rules should keep the app coherent before visual styling begins.

### Section Container

Used on Today, Insights, and Templates.

Should include:

- Section title
- Optional summary line
- Optional primary action
- Expand or collapse affordance when needed

Rule:

- Sections should behave like modular blocks, not heavy card walls

### Daily Summary Block

Used on Today and historical day detail.

Should include:

- Date or day context
- Phase label
- Completion snapshot
- Lightweight template context

Rule:

- This is orientation first, not analytics

### Numeric Input Row

Used on body, meal, and set entry surfaces.

Should include:

- Label
- Value field
- Unit
- Inline validation space

Rule:

- Labels and units must remain visible while editing

### Meal Card

Should include:

- Meal name
- Logged time
- Macro summary
- Template marker when relevant
- Edit entry point

Rule:

- The collapsed card must already be useful without opening details

### Exercise Card

Should include:

- Exercise title
- Optional metadata such as notes or template origin
- Repeated set rows
- Add set action

Rule:

- The card should prioritize loggable rows over ornamental structure

### Set Row

Should include:

- Set index
- Weight
- Reps
- Optional RPE
- Row actions if needed

Rule:

- Rows must be easy to edit one-handed and easy to compare vertically

### Chart Card

Should include:

- Metric title
- Time range context
- Main chart
- Reference line or target when helpful
- Short footer summary

Rule:

- Every chart should answer one question clearly

### Template Card

Should include:

- Template name
- Template type
- Short description or included content summary
- Last-used or default marker when useful

Rule:

- Cards should help the user choose quickly, not read long descriptions

### Empty State Panel

Should include:

- Clear reason the section is empty
- One obvious next action
- Minimal supporting copy

Rule:

- Empty states should motivate action without sounding judgmental

## Wireframe State Coverage Checklist

The first design pass should not stop at one ideal-state mockup per page. These state variants need explicit frames.

### Today

- Empty new day
- Partial day with meals missing
- Training day with completed workout
- Rest day with no training section emphasis

### Meal Entry

- Fresh manual entry
- Template-applied entry
- Entry expanded with food items
- Validation error state

### Active Workout

- Fresh workout from template
- Mid-session with several completed sets
- Custom exercise added
- Finish-review state

### History

- Empty history
- Mixed complete and partial days
- Selected past day

### Insights Overview

- First-use no-data state
- Normal state with three to five chart cards

### Weekly Review

- Strong week with clear progress
- Mixed-signal week where bodyweight and training tell a more nuanced story

### Templates Home

- No templates yet
- Normal populated state with default markers

## P0 Low-Fidelity Screen Specifications

This section is more concrete than the blueprint section above. It is intended to let a designer build first-pass mobile wireframes without guessing layout hierarchy or interaction priority.

### Today: Low-Fidelity Spec

**Primary job**

- Let the user understand today's state instantly
- Let the user act on the next missing thing with one tap

**Screen hierarchy**

1. Top app bar
   - Title: `Today`
   - Date context
   - Overflow entry
2. Daily summary block
   - Phase label
   - Logging progress snapshot
   - Form template indicator
3. Body section
4. Meals section
5. Training section
6. Notes section
7. Bottom navigation and Quick Add trigger

**Above-the-fold content**

- Daily summary block
- Body section summary
- Meals section header and first meal card

**Default section behavior**

- Body section starts expanded if no weight is logged yet
- Meals section starts expanded to summary level
- Training section starts collapsed if empty, summary-open if workout exists
- Notes section stays collapsed unless notes already exist

**Daily summary block layout**

- Left side: date and phase
- Right side: completion state and form template link
- Secondary line: short summary such as `2 meals logged • no workout yet`

**Body section layout**

- One dominant weight row or card
- One secondary row for body fat and waist
- One tertiary row for recovery or activity metrics
- Primary CTA anchored to the section header or footer

**Meals section layout**

- Section header with add-meal action
- Four meal cards in vertical order
- Each meal card should show:
  - Meal name
  - Logged time or empty hint
  - Calories and protein first
  - Optional template marker
  - Status state

**Training section layout**

- If empty:
  - empty-state block
  - start-workout CTA
- If in progress:
  - session summary strip
  - continue-workout CTA
  - optional last edited timestamp
- If complete:
  - compact session summary
  - top-set or volume highlight
  - open-detail CTA

**Notes section layout**

- One-line preview when collapsed
- Full text field when expanded

**Interaction priorities**

1. Record weight
2. Add the next meal
3. Start or continue workout
4. Fill notes or auxiliary metrics

**Wireframe rules**

- The most important missing action should be obvious without scrolling
- The screen should never look like a long editable spreadsheet
- Summary states must remain readable when most sections are collapsed

### Quick Add: Low-Fidelity Spec

**Primary job**

- Get the user into the right capture flow in one decision

**Sheet structure**

1. Title row
2. Four action buttons
3. Optional small recent-context row later if testing proves it useful

**Action card structure**

- Icon or short visual marker
- Action title
- Short supporting line only if needed

**Ordering**

- Record weight
- Add meal
- Start workout
- Add recovery or activity metric

**Wireframe rules**

- Actions should fit in one glance
- This surface should not require scrolling on a typical phone

### Meal Entry: Low-Fidelity Spec

**Primary job**

- Capture one meal in under a minute

**Form structure**

1. Sheet header with meal context
2. Meal slot selector
3. Time selector
4. Calories field
5. Protein field
6. Optional carbs and fat row
7. Template chip row
8. Notes field
9. Expandable food-item block
10. Primary save action

**Default behavior**

- Open on totals-first mode
- Auto-focus the first numeric field after slot selection when possible
- Keep food-item detail collapsed by default

**Field priority**

- Calories and protein are first-class
- Carbs and fat are secondary
- Notes and food items are tertiary

**Template behavior**

- Template entry should appear before notes
- Applied template should visibly change the sheet state

**Save behavior**

- Save CTA should remain reachable when keyboard is open
- Dismiss should not silently lose entered values

**Wireframe rules**

- Use a clear vertical rhythm
- Avoid side-by-side fields except for carb and fat
- Do not visually punish the user for leaving food items empty

### Active Workout: Low-Fidelity Spec

**Primary job**

- Make real-time set logging fast enough to use inside the gym

**Screen hierarchy**

1. Workout header
   - Workout name
   - Elapsed time
   - Overflow actions
2. Exercise list
3. Add exercise action
4. Sticky finish-workout action

**Exercise card structure**

- Exercise title row
- Optional metadata row
- Set list
- Add-set action

**Set row structure**

- Set number
- Weight field
- Reps field
- Optional RPE field
- Optional row action

**Default exercise behavior**

- Template-based exercises arrive pre-created
- Completed set rows remain visible and editable
- Custom exercises append to the bottom by default

**Density rules**

- Set rows should be compact
- Header spacing should be tighter than Today
- The exercise list should dominate the screen height

**Save and exit rules**

- Set edits should auto-save or commit with minimal friction
- Finishing the workout should be an explicit action
- Leaving the screen mid-session should preserve progress

**Wireframe rules**

- Avoid full-width decorative cards between exercises
- Make row alignment strong so weights and reps compare easily
- Use one stable bottom action instead of many floating actions

### History: Low-Fidelity Spec

**Primary job**

- Help the user find a day and understand what happened on it

**Screen hierarchy**

1. Top app bar
2. Date navigation region
3. Calendar strip or month entry
4. Timeline of recent days

**Timeline row structure**

- Date label
- Weight summary
- Meal status
- Workout indicator
- Completion state chip

**Default behavior**

- Recent days appear first
- Today remains easy to distinguish from past days
- Selecting a day opens historical day detail

**Wireframe rules**

- Rows should prioritize legibility over density
- Missing data should be visible without turning the list into a warning board

### Insights Overview: Low-Fidelity Spec

**Primary job**

- Let the user judge whether the cut is on track

**Screen hierarchy**

1. Top app bar
2. Summary strip
3. Weight chart card
4. Nutrition chart card
5. Training chart card
6. Optional recovery or activity chart card
7. Weekly review entry card

**Summary strip content**

- Current 7-day bodyweight direction
- One concise comparison to the previous period

**Chart card structure**

- Metric title
- Timeframe
- Main visual
- Short takeaway footer

**Default behavior**

- Weight card appears first
- Weekly review entry should be visible without going to the bottom of a very long page

**Wireframe rules**

- One chart card should answer one question
- Avoid tiny multi-metric combo charts in the first pass

### Weekly Review: Low-Fidelity Spec

**Primary job**

- Turn the week into a readable review, not just more charts

**Screen hierarchy**

1. Week selector
2. Headline state card
3. Body section
4. Intake section
5. Training section
6. Recovery or activity section
7. Notes section

**Headline state card should contain**

- Average weight change
- One review label
- One short supporting line

**Section structure**

- Key stat row
- Comparison to prior week
- Optional supporting context

**Wireframe rules**

- Sections should read top to bottom like a review checklist
- The headline must not overshadow the rest of the week breakdown

### Templates Home: Low-Fidelity Spec

**Primary job**

- Help the user manage templates without confusing structure and content

**Screen hierarchy**

1. Top app bar
2. Form templates group
3. Training templates group
4. Meal templates group
5. Create-template actions

**Group structure**

- Group title
- Short group description
- Template list or cards
- Add action

**Wireframe rules**

- Form templates must look categorically different from meal and training templates
- Default templates should be identifiable in one glance

## Interaction Behavior Rules

These rules should be honored across wireframes so the flows behave coherently.

### Save Model

- Embedded edits on Today can auto-save
- Bottom-sheet entry flows should use an explicit save action
- Active workout row edits should save with near-zero friction

### Back Navigation

- If nothing changed, back closes immediately
- If a sheet has unsaved manual entry, the user should get a lightweight discard or continue prompt
- Leaving an active workout should preserve progress unless the user explicitly discards it

### Keyboard Behavior

- Numeric fields should summon numeric-first keyboards
- Focus should move in a practical logging order
- Save actions should remain visible or easily reachable while typing

### Scroll Behavior

- Each screen should have one dominant vertical scroll region
- Nested scrolling should be minimized, especially in workout logging

### Quick Add Visibility

- Quick Add remains visible on top-level screens
- Quick Add should be hidden or de-emphasized inside full-screen workout logging

### Feedback Behavior

- Successful low-risk actions can use toast or snackbar confirmation
- Validation should appear inline near the field
- Failure copy should say what happened and what can be retried

## Design Priorities For Wireframes

Design order should be:

1. App shell and Today
2. Quick Add and meal entry
3. Active workout
4. History and historical day detail
5. Insights overview and weekly review
6. Templates home
7. Template editors, metrics manager, settings, and export

If this order changes, the risk is that visual design time gets spent on management screens before the real product loop feels good.

## Open Visual Direction Notes

The structural blueprint above should be locked before a full visual system pass.

When visual design starts, the style should likely favor:

- Functional athletic energy over glossy marketing polish
- Medium information density, not oversized cards everywhere
- Strong numeric readability
- Clear section hierarchy
- Restrained accent usage

The visual design pass should explicitly avoid:

- Purple-gradient SaaS defaults
- Over-rounded generic cards
- Centered-everything empty space patterns
- Decorative motion inside data entry flows

## Relationship To Product Spec

The product definition lives in [docs/superpowers/specs/2026-03-22-local-fat-loss-tracker-design.md](docs/superpowers/specs/2026-03-22-local-fat-loss-tracker-design.md).

This `DESIGN.md` is the UI/UX companion document. It should guide:

- Wireframes
- Component inventory
- Screen prioritization
- High-fidelity design planning

Implementation planning should only start after this document is reviewed and adjusted as needed.
