# Local-First Fat Loss Tracker Design

## Overview

This project is a local-first Android app for one primary user: the app owner during a fat-loss phase.

The app records:

- Body metrics such as weight, body fat, and waist
- Meals and nutrition
- Training sessions and strength progression
- A small set of recovery and activity metrics

The first release is intentionally narrow. It should make daily logging sustainable for 8-12 weeks, then make weekly progress easy to evaluate. It is not a generic fitness platform, a social app, or a cloud product.

## Product Goals

- Make logging friction low enough that the user will actually keep using it.
- Keep bodyweight, meals, training, and context in one coherent daily view.
- Support both structured daily logging and in-the-moment quick capture.
- Reuse stable daily structure through templates.
- Provide lightweight trend analysis and weekly review.
- Support export of raw data for deeper external analysis later.

## Non-Goals For V1

- Accounts, cloud sync, or multi-device sync
- Public multi-user product concerns
- Barcode scanning or a large online food database
- Wearable integrations or health platform integrations
- Advanced training program generation or periodization logic
- AI-generated long-form recommendations
- Fully custom dynamic form building

## Core Product Shape

The app is organized around one main idea: `today` is the center of logging, but not the only entry point.

Primary navigation:

- `Today`: the main home and daily logging page
- `History`: browse, review, and backfill past days
- `Insights`: trends and lightweight weekly review
- `Templates`: manage form templates and content templates

Global action:

- `Quick Add`: fast entry for weight, one meal, one training session, or one recovery/activity metric from anywhere

This combines two needs:

- A stable daily structure
- Fast logging when eating or training in the moment

## Information Architecture

### Today

The Today page is the primary entry point and the default home screen.

It is composed of five sections:

1. `Top Summary`
   - Date
   - Current phase tag such as `cut`
   - Completion summary for today's logging

2. `Body Status`
   - Weight
   - Body fat
   - Waist
   - Recovery and activity metrics shown by the active form template

3. `Meals`
   - Breakfast
   - Lunch
   - Dinner
   - Snacks

4. `Training`
   - Start from a training template
   - Or create an empty session
   - Review active or completed session summary

5. `Daily Notes`
   - Hunger
   - Energy
   - Schedule changes
   - Any context useful during weekly review

The page should use progressive disclosure. It should not force every detail open at once.

### History

History supports:

- Calendar-based day lookup
- Timeline-based review
- Backfilling missing records
- Reopening a past day to inspect or edit its entries

### Insights

Insights is read-only in spirit. It should not become a second logging surface.

It contains:

- Trend charts
- Weekly review
- No heavy recommendation engine

### Templates

Templates is a management screen, not the primary place where the user logs data.

It manages:

- Form templates
- Content templates

## Data Model

The data model should keep strong business meaning. V1 should not use a generic form-engine schema for core data.

### Day-Centered Container

`DayRecord`

- One record per local date
- Holds day-level metadata
- Links the day's sections together
- References the active form template
- Stores day notes and current phase tag

### Body Data

`BodyCheckin`

- Weight
- Body fat
- Waist
- Timestamp
- Usually one main entry per day, while still allowing future flexibility

### Meals And Nutrition

`MealLog`

- Meal type such as breakfast, lunch, dinner, snack
- Logged time
- Calories
- Protein
- Optional carbs
- Optional fat
- Notes
- Optional source template snapshot

`FoodItem`

- Child of a meal
- Food name
- Portion or weight
- Calories
- Protein
- Carbs
- Fat

Meal logging is hybrid by design:

- Fast path: record meal-level totals only
- Detailed path: expand into food items when needed

For V1, `MealLog` totals remain the primary values used by Insights and export. `FoodItem` rows are optional supporting detail for recall and future analysis. They do not need to turn V1 into a strict nutrition ledger.

### Training

`WorkoutSession`

- Start time
- End time
- Notes
- Source training template snapshot

`ExerciseLog`

- Child of a workout session
- Exercise name
- Order
- Notes
- Whether it came from a template

`SetLog`

- Child of an exercise log
- Weight
- Reps
- Optional RPE
- Optional notes

Strength and training analysis should derive from these logs instead of being entered separately as primary data.

### Recovery And Activity

`MetricDefinition`

- Small custom metric definitions
- Examples: sleep hours, steps, mood, resting heart rate, cardio minutes
- Includes type, unit, and category

`MetricValue`

- Actual value for a specific day
- References a metric definition

Custom fields are restricted to auxiliary metrics, not core body, meal, or training structures.

## Template System

Templates are split into two layers with strict responsibilities.

### Form Templates

`FormTemplate` controls the structure of the daily logging experience.

Examples:

- Training day template
- Rest day template

Responsibilities:

- Which sections appear on Today
- Field order
- Default expanded or collapsed state
- Default values for supported fields
- Which custom metrics are shown

Form templates shape the UI for the day. They do not generate actual training sets or meals.

Each day should have exactly one active form template. When a new day is first opened, the app should apply the user's default form template or let the user pick one. Changing the form template later must not rewrite existing meal, training, or metric data for that day. It only changes the day's structure and visible prompts.

### Content Templates

`ContentTemplate` provides reusable content that can be applied during entry.

Examples:

- Leg day training template
- Push day training template
- High-protein breakfast template
- Training day dinner template

Responsibilities:

- Pre-fill exercises and planned sets
- Pre-fill meals or meal macros
- Speed up repeated entry

After application, the created records become normal editable records. Later template edits must not mutate history.

### Template Versioning

Each applied template should store:

- Template id
- Template version or snapshot
- Applied timestamp

This preserves historical truth even if the template changes later.

## Core User Flows

### Daily Logging Flow

The daily flow centers on Today:

1. Open Today
2. See body status, meal status, training status, and missing sections
3. Fill sections directly or use Quick Add
4. Review the complete day in one place

### Quick Add Flow

Quick Add exists for high-frequency moments:

- Record bodyweight after weighing in
- Record one meal after eating
- Start training when arriving at the gym
- Add one recovery or activity metric quickly

Quick Add should stay narrow. If it tries to expose every action, the app becomes fragmented.

### Meal Logging Flow

Recommended interaction:

1. Select meal slot
2. Enter calories, protein, and optional carbs and fat
3. Add notes if needed
4. Optionally expand into food items
5. Optionally apply a meal template before editing

### Training Logging Flow

Recommended interaction:

1. Start a workout session
2. Choose a training template or empty session
3. Generate exercise list and planned sets
4. Log actual sets during training
5. Adjust sets, weights, reps, or exercise order freely
6. Finish the session and view a summary

Templates provide a starting point, not a constraint.

### Backfill Flow

The user must be able to return to History and complete missing data without friction.

Missing or partial sections should be detectable, but the app should not behave like a task manager.

## Insights And Weekly Review

The analytics scope for V1 stays intentionally lightweight.

### Trend Views

The app should show:

- Daily weight plus 7-day moving average
- Body fat trend when available
- Daily calories plus weekly average
- Daily protein plus target line
- Training volume trend
- Core lift performance or estimated 1RM trend
- Optional steps and sleep trend

Trend interpretation should emphasize stable direction over single-day noise.

### Weekly Review

The weekly review should summarize:

- Average bodyweight vs previous week
- Body fat or waist change when recorded
- Average calories
- Average protein
- Training count
- Training volume change
- Core lift status
- Step and sleep overview
- User notes worth carrying forward

V1 may generate a small rules-based summary label, such as:

- `fat loss on track`
- `weight down, training quality slipping`
- `calories high, bodyweight flat`

This should be rules-based, not an AI reporting feature.

## Export

V1 should support raw data export in:

- CSV
- JSON

Export principles:

- Raw records come first
- Derived metrics are optional additions, not replacements
- Time fields and template source fields should be preserved
- Each core entity should export cleanly enough for external analysis

Suggested CSV outputs:

- `day_records.csv`
- `body_checkins.csv`
- `meal_logs.csv`
- `food_items.csv`
- `workout_sessions.csv`
- `exercise_logs.csv`
- `set_logs.csv`
- `metric_values.csv`

## Error Handling And Validation

V1 is local-first, so the main failure modes are input errors and local persistence issues rather than network issues.

The design should account for:

- Invalid values
  - Example: negative bodyweight, negative calories, impossible reps
- Partial records
  - Example: a workout created but not fully completed
- Template drift
  - A template changes after earlier records were created from it
- Missing optional detail
  - Example: a meal without food items is still valid

Validation rules should be strict where meaning matters and relaxed where logging speed matters.

Recommended behavior:

- Save drafts or partial records safely
- Validate hard constraints on numeric fields
- Do not force unnecessary fields
- Mark sections as empty or partial instead of blocking the whole day
- Recover gracefully if one section fails to save without discarding the rest of the day

## Architecture Direction

Although V1 is Android-only, the architecture should preserve a clean domain model so future web or desktop analysis is possible.

Planning should assume:

- Local-first storage
- Clear separation between domain data and derived analytics
- Domain modules aligned to body, meals, training, metrics, templates, and insights
- Export built on top of stable domain entities rather than UI-specific structures

## Testing And Acceptance Focus

The first implementation plan should prioritize test coverage around the following:

### Domain Behavior

- Day record creation and retrieval by local date
- Meal totals and optional food-item detail
- Workout session, exercise, and set persistence
- Template application with historical snapshot preservation
- Derived training metrics from set logs

### UX-Critical Flows

- Record a bodyweight entry quickly
- Record a meal in under a minute
- Start and complete a workout session with template-based sets
- Backfill a missed day
- Review a weekly summary without ambiguity

### Data Integrity

- Exported CSV and JSON contain all raw data needed for external analysis
- Editing templates does not rewrite historical logs
- Partial logs do not corrupt the day container

## Success Criteria

The product is successful if:

- Logging is consistent for 8-12 weeks
- A meal can be captured with low friction
- A workout can be logged without the app becoming a burden
- The user can tell whether fat loss is progressing by looking at Insights
- The exported data is trustworthy enough for deeper analysis outside the app

## Recommendation

Build V1 as a focused local-first Android app with:

- Today-centered logging
- Quick Add for in-the-moment capture
- Strong semantic data model
- Two-layer template system
- Lightweight trend analysis and weekly review
- Raw data export

This keeps the product narrow, useful, and extensible without turning V1 into a generic health platform.

## Design Companion

The UI and UX surface inventory for this product is maintained in [DESIGN.md](../../../DESIGN.md).

That document should guide:

- Wireframes
- Screen prioritization
- Component inventory
- High-fidelity design planning

Implementation planning should use this spec and `DESIGN.md` together.
