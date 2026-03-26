// ignore_for_file: constant_identifier_names
//
// Trainlytics — Material Design 3 Color System
// Seed Color: #00E676
// Generated: 2026-03-26T15:46:45.670Z
// Source: @material/material-color-utilities v0.2.7
// DO NOT EDIT — regenerate with `node generate-colors.mjs`
//

import 'dart:ui';
import 'package:flutter/material.dart';

/// Holds all Material Design 3 semantic color tokens for the app,
/// derived from the seed color #00E676.
class TrainlyticsColors {
  TrainlyticsColors._();

  // ──────────────────────────────────────────────────
  // Light Theme
  // ──────────────────────────────────────────────────

  static const Color lightPrimary = Color(0xFF006D35);
  static const Color lightOnPrimary = Color(0xFFFFFFFF);
  static const Color lightPrimaryContainer = Color(0xFF62FF96);
  static const Color lightOnPrimaryContainer = Color(0xFF00210B);
  static const Color lightSecondary = Color(0xFF506352);
  static const Color lightOnSecondary = Color(0xFFFFFFFF);
  static const Color lightSecondaryContainer = Color(0xFFD3E8D2);
  static const Color lightOnSecondaryContainer = Color(0xFF0E1F12);
  static const Color lightTertiary = Color(0xFF3A656E);
  static const Color lightOnTertiary = Color(0xFFFFFFFF);
  static const Color lightTertiaryContainer = Color(0xFFBDEAF5);
  static const Color lightOnTertiaryContainer = Color(0xFF001F25);
  static const Color lightError = Color(0xFFBA1A1A);
  static const Color lightOnError = Color(0xFFFFFFFF);
  static const Color lightErrorContainer = Color(0xFFFFDAD6);
  static const Color lightOnErrorContainer = Color(0xFF410002);
  static const Color lightBackground = Color(0xFFFCFDF7);
  static const Color lightOnBackground = Color(0xFF191C19);
  static const Color lightSurface = Color(0xFFFCFDF7);
  static const Color lightOnSurface = Color(0xFF191C19);
  static const Color lightSurfaceVariant = Color(0xFFDDE5DA);
  static const Color lightOnSurfaceVariant = Color(0xFF414941);
  static const Color lightOutline = Color(0xFF717970);
  static const Color lightOutlineVariant = Color(0xFFC1C9BF);
  static const Color lightInversePrimary = Color(0xFF00E475);
  static const Color lightInverseSurface = Color(0xFF2E312E);
  static const Color lightInverseOnSurface = Color(0xFFF0F1EC);
  static const Color lightSurfaceTint = Color(0xFF006D35);
  static const Color lightSurfaceDim = Color(0xFFD9DBD5);
  static const Color lightSurfaceBright = Color(0xFFF9FAF4);
  static const Color lightSurfaceContainerLowest = Color(0xFFFFFFFF);
  static const Color lightSurfaceContainerLow = Color(0xFFF3F4EF);
  static const Color lightSurfaceContainer = Color(0xFFEDEEE9);
  static const Color lightSurfaceContainerHigh = Color(0xFFE7E9E3);
  static const Color lightSurfaceContainerHighest = Color(0xFFE2E3DE);
  static const Color lightScrim = Color(0xFF000000);
  static const Color lightShadow = Color(0xFF000000);

  // ──────────────────────────────────────────────────
  // Dark Theme
  // ──────────────────────────────────────────────────

  static const Color darkPrimary = Color(0xFF00E475);
  static const Color darkOnPrimary = Color(0xFF003918);
  static const Color darkPrimaryContainer = Color(0xFF005226);
  static const Color darkOnPrimaryContainer = Color(0xFF62FF96);
  static const Color darkSecondary = Color(0xFFB7CCB7);
  static const Color darkOnSecondary = Color(0xFF233426);
  static const Color darkSecondaryContainer = Color(0xFF394B3B);
  static const Color darkOnSecondaryContainer = Color(0xFFD3E8D2);
  static const Color darkTertiary = Color(0xFFA2CED8);
  static const Color darkOnTertiary = Color(0xFF01363E);
  static const Color darkTertiaryContainer = Color(0xFF204D56);
  static const Color darkOnTertiaryContainer = Color(0xFFBDEAF5);
  static const Color darkError = Color(0xFFFFB4AB);
  static const Color darkOnError = Color(0xFF690005);
  static const Color darkErrorContainer = Color(0xFF93000A);
  static const Color darkOnErrorContainer = Color(0xFFFFB4AB);
  static const Color darkBackground = Color(0xFF191C19);
  static const Color darkOnBackground = Color(0xFFE2E3DE);
  static const Color darkSurface = Color(0xFF191C19);
  static const Color darkOnSurface = Color(0xFFE2E3DE);
  static const Color darkSurfaceVariant = Color(0xFF414941);
  static const Color darkOnSurfaceVariant = Color(0xFFC1C9BF);
  static const Color darkOutline = Color(0xFF8B938A);
  static const Color darkOutlineVariant = Color(0xFF414941);
  static const Color darkInversePrimary = Color(0xFF006D35);
  static const Color darkInverseSurface = Color(0xFFE2E3DE);
  static const Color darkInverseOnSurface = Color(0xFF2E312E);
  static const Color darkSurfaceTint = Color(0xFF00E475);
  static const Color darkSurfaceDim = Color(0xFF111411);
  static const Color darkSurfaceBright = Color(0xFF373A36);
  static const Color darkSurfaceContainerLowest = Color(0xFF0C0F0C);
  static const Color darkSurfaceContainerLow = Color(0xFF191C19);
  static const Color darkSurfaceContainer = Color(0xFF1E201D);
  static const Color darkSurfaceContainerHigh = Color(0xFF282B27);
  static const Color darkSurfaceContainerHighest = Color(0xFF333532);
  static const Color darkScrim = Color(0xFF000000);
  static const Color darkShadow = Color(0xFF000000);

  // ──────────────────────────────────────────────────
  // Seed Color
  // ──────────────────────────────────────────────────

  static const Color seed = Color(0xFF00E676);

  // ──────────────────────────────────────────────────
  // Material 3 ColorScheme Builders
  // ──────────────────────────────────────────────────

  /// Creates a [ColorScheme.light] from the Trainlytics color tokens.
  static ColorScheme get lightColorScheme => const ColorScheme(
        brightness: Brightness.light,
        primary: lightPrimary,
        onPrimary: lightOnPrimary,
        primaryContainer: lightPrimaryContainer,
        onPrimaryContainer: lightOnPrimaryContainer,
        secondary: lightSecondary,
        onSecondary: lightOnSecondary,
        secondaryContainer: lightSecondaryContainer,
        onSecondaryContainer: lightOnSecondaryContainer,
        tertiary: lightTertiary,
        onTertiary: lightOnTertiary,
        tertiaryContainer: lightTertiaryContainer,
        onTertiaryContainer: lightOnTertiaryContainer,
        error: lightError,
        onError: lightOnError,
        errorContainer: lightErrorContainer,
        onErrorContainer: lightOnErrorContainer,
        background: lightBackground,
        onBackground: lightOnBackground,
        surface: lightSurface,
        onSurface: lightOnSurface,
        surfaceVariant: lightSurfaceVariant,
        onSurfaceVariant: lightOnSurfaceVariant,
        outline: lightOutline,
        outlineVariant: lightOutlineVariant,
        inversePrimary: lightInversePrimary,
        inverseSurface: lightInverseSurface,
        inverseOnSurface: lightInverseOnSurface,
        surfaceTint: lightSurfaceTint,
        scrim: lightScrim,
        shadow: lightShadow,
      );

  /// Creates a [ColorScheme.dark] from the Trainlytics color tokens.
  static ColorScheme get darkColorScheme => const ColorScheme(
        brightness: Brightness.dark,
        primary: darkPrimary,
        onPrimary: darkOnPrimary,
        primaryContainer: darkPrimaryContainer,
        onPrimaryContainer: darkOnPrimaryContainer,
        secondary: darkSecondary,
        onSecondary: darkOnSecondary,
        secondaryContainer: darkSecondaryContainer,
        onSecondaryContainer: darkOnSecondaryContainer,
        tertiary: darkTertiary,
        onTertiary: darkOnTertiary,
        tertiaryContainer: darkTertiaryContainer,
        onTertiaryContainer: darkOnTertiaryContainer,
        error: darkError,
        onError: darkOnError,
        errorContainer: darkErrorContainer,
        onErrorContainer: darkOnErrorContainer,
        background: darkBackground,
        onBackground: darkOnBackground,
        surface: darkSurface,
        onSurface: darkOnSurface,
        surfaceVariant: darkSurfaceVariant,
        onSurfaceVariant: darkOnSurfaceVariant,
        outline: darkOutline,
        outlineVariant: darkOutlineVariant,
        inversePrimary: darkInversePrimary,
        inverseSurface: darkInverseSurface,
        inverseOnSurface: darkInverseOnSurface,
        surfaceTint: darkSurfaceTint,
        scrim: darkScrim,
        shadow: darkShadow,
      );
}
