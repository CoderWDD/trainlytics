# Design System Strategy: Performance Professional

## 1. Overview & Creative North Star
The Creative North Star for this design system is **"The Kinetic Lab."** 

This isn't a social media app for fitness influencers; it is a high-precision instrument for athletes. We are moving away from the "template" aesthetic of fitness apps and toward a high-end editorial feel. We achieve this through **intentional asymmetry**, where data density is balanced by expansive "breathing room," and a typography scale that treats heart rate and macro counts as headline-worthy achievements. 

The goal is a layout that feels "local-first" and private—an intimate, powerful cockpit for the user’s physical performance. We avoid "gym-bro" tropes (heavy weights, sweat textures, aggressive italics) in favor of sleek, mathematical utility and layered depth.

---

## 2. Colors & Surface Philosophy
The palette is built on a foundation of obsidian and slate, providing a high-contrast stage for the "Action Green" primary accent.

### The "No-Line" Rule
To maintain a premium, editorial feel, **1px solid borders are strictly prohibited for sectioning.** We do not use lines to separate content. Boundaries must be defined through:
*   **Background Shifts:** Placing a `surface-container-low` card on a `surface` background.
*   **Tonal Transitions:** Using the hierarchy of container tokens to denote nesting.

### Surface Hierarchy & Nesting
Think of the UI as a series of physical layers—stacked sheets of frosted glass. 
*   **Base:** `surface` (#0e0e0e) for the global background.
*   **Sections:** Use `surface-container-low` (#131313) for large content areas.
*   **Primary Cards:** Use `surface-container` (#1a1a1a) or `surface-container-high` (#20201f) to draw the eye to critical data.
*   **Nesting:** If a chart sits inside a card, the chart background should be `surface-container-highest` (#262626).

### The "Glass & Gradient" Rule
Flat color is the enemy of premium design. 
*   **Glassmorphism:** For floating elements (like a sticky navigation bar or a "Record Workout" overlay), use `surface` at 70% opacity with a `20px` backdrop-blur. 
*   **Signature Textures:** Main CTAs and progress indicators should utilize a subtle linear gradient from `primary` (#3fff8b) to `primary-container` (#13ea79) at a 135° angle. This adds "visual soul" and a sense of kinetic energy.

---

## 3. Typography: Data as Hero
This design system utilizes a dual-typeface strategy to separate "The Story" (Manrope) from "The Data" (Inter).

*   **Display & Headlines (Manrope):** These are the "Editorial" moments. Use `display-lg` for daily streaks or weight goals. The geometric nature of Manrope feels modern and authoritative.
*   **Titles & Body (Inter):** Inter is the workhorse. Use `title-md` for data labels. Inter’s high x-height ensures legibility even when the user is mid-sprint.
*   **Hierarchy through Weight:** Avoid using color to show hierarchy (e.g., light grey text). Instead, use `Font-Weight: 600` for primary data and `Font-Weight: 400` for secondary labels, keeping both in `on-surface` or `on-surface-variant`.

---

## 4. Elevation & Depth
We convey importance through **Tonal Layering**, not structural lines.

*   **The Layering Principle:** Depth is achieved by "stacking" container tiers. A `surface-container-lowest` card placed on a `surface-container-low` section creates a soft, natural lift without the "heaviness" of a shadow.
*   **Ambient Shadows:** When a floating action is required, shadows must be ultra-diffused. 
    *   *Spec:* `0px 12px 32px rgba(0, 0, 0, 0.4)`. The shadow color is never pure black; it is a tinted version of the background.
*   **The "Ghost Border" Fallback:** If a border is required for accessibility (e.g., an input field), use the `outline-variant` (#484847) token at **20% opacity**. Never use a 100% opaque border.

---

## 5. Components

### Buttons
*   **Primary:** Gradient of `primary` to `primary-container`. `Roundedness: md` (0.75rem). No shadow; the vibrancy provides the "lift."
*   **Secondary:** `surface-container-highest` with `on-surface` text. This feels like a part of the UI, not a separate "widget."
*   **Tertiary:** Ghost style. `on-surface` text with no container. Use for low-priority actions like "Cancel."

### Performance Cards & Lists
*   **Rule:** Forbid the use of divider lines. 
*   **Implementation:** Separate list items using `spacing-3` (0.6rem) and a background shift to `surface-container-low`. 
*   **Nesting:** A "Macro Breakdown" inside a "Nutrition Card" should use a `surface-variant` background to distinguish it from the parent card.

### Input Fields
*   **State:** Use `surface-container-highest` as the base. 
*   **Focus:** Instead of a thick border, use a 2px `primary` underline or a subtle `primary` glow (using the `surface-tint` token).

### Metrics & Progress (Signature Components)
*   **Progress Rings:** Use `primary` for the active track and `surface-container-highest` for the empty track.
*   **Status Indicators:** Small 8px dots using `error` (#ff716c) for deficits and `tertiary` (#6e9bff) for hydration/neutral metrics.

---

## 6. Do's and Don'ts

### Do
*   **Embrace Asymmetry:** Align primary stats to the left and secondary "insight" text to the right with a smaller font scale.
*   **Use Whitespace as a Tool:** Use `spacing-16` (3.5rem) between major sections to prevent data fatigue.
*   **Local-First Visuals:** Use `on-surface-variant` for helper text to emphasize that data is stored locally and securely.

### Don't
*   **No Dividers:** Never use a horizontal rule `<hr>` to separate content. Use a `0.5rem` background gap.
*   **No High-Contrast Borders:** Never use `outline` at 100% opacity. It breaks the "Kinetic Lab" immersion.
*   **No Generic Grids:** Avoid a 50/50 split layout. Use a 60/40 or 70/30 split to create a more dynamic, editorial rhythm.
*   **No Aggressive Shadows:** If the shadow is the first thing a user notices, it is too heavy.

---

## 7. Spacing & Rhythm
Rhythm in this design system is driven by the **2.5 unit (0.5rem)**.
*   **Internal Padding:** Use `spacing-4` (0.9rem) for card internals.
*   **External Margins:** Use `spacing-6` (1.3rem) for page gutters.
*   **Section Gaps:** Use `spacing-10` (2.25rem) to separate distinct data clusters (e.g., "Workouts" from "Nutrition").