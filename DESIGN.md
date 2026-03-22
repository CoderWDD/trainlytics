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
