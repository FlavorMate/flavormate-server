/* Licensed under AGPLv3 2024 */
package de.flavormate.aa_interfaces.models;

/** Represents a software version with major, minor, patch, and build components. */
public record Version(int major, int minor, int patch, int build) {

  /**
   * Parses a version string in the format "major.minor.patch+build" and returns a Version object.
   *
   * @param s the version string to parse
   * @return a Version object containing the parsed major, minor, patch, and build numbers
   */
  public static Version parse(String s) {
    var parts = s.split("\\+");

    var versions = parts[0].split("\\.");

    var major = Integer.parseInt(versions[0]);
    var minor = Integer.parseInt(versions[1]);
    var patch = Integer.parseInt(versions[2]);
    var build = Integer.parseInt(parts[1]);
    return new Version(major, minor, patch, build);
  }

  /**
   * Checks whether the provided major version is the same as the instance's major version.
   *
   * @param other the major version to compare
   * @return true if the provided major version matches the instance's major version, otherwise
   *     false
   */
  public boolean sameMajor(int other) {
    return major == other;
  }

  /**
   * Checks if the minor version number is the same as the specified value.
   *
   * @param other the minor version number to compare against
   * @return true if the minor version number is the same as the specified value, false otherwise
   */
  public boolean sameMinor(int other) {
    return minor == other;
  }

  /**
   * Checks if the patch version of this instance is the same as the provided value.
   *
   * @param other the patch version to compare against.
   * @return true if the patch version matches the provided value, false otherwise.
   */
  public boolean samePatch(int other) {
    return patch == other;
  }

  /**
   * Checks if this version's build number is the same as the specified build number.
   *
   * @param other the build number to compare against
   * @return true if the build numbers are the same, false otherwise
   */
  public boolean sameBuild(int other) {
    return build == other;
  }

  /**
   * Compares this version with another version to determine if this version is greater.
   *
   * @param other the version to compare with this version
   * @return true if this version is greater than the other version, false otherwise
   */
  public boolean gt(Version other) {
    if (major > other.major) {
      return true;
    }
    if (major < other.major) {
      return false;
    }
    if (minor > other.minor) {
      return true;
    }
    if (minor < other.minor) {
      return false;
    }
    if (patch > other.patch) {
      return true;
    }
    if (patch < other.patch) {
      return false;
    }
    return build > other.build;
  }

  /**
   * Returns a string representation of the version in the format "major.minor.patch+build".
   *
   * @return a string representing the version.
   */
  @Override
  public String toString() {
    return major + "." + minor + "." + patch + "+" + build;
  }
}
