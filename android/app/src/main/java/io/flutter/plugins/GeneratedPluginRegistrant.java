package io.flutter.plugins;

import io.flutter.plugin.common.PluginRegistry;
import com.flutteramazons3.FlutterAmazonS3Plugin;

/**
 * Generated file. Do not edit.
 */
public final class GeneratedPluginRegistrant {
  public static void registerWith(PluginRegistry registry) {
    if (alreadyRegisteredWith(registry)) {
      return;
    }
    FlutterAmazonS3Plugin.registerWith(registry.registrarFor("com.flutteramazons3.FlutterAmazonS3Plugin"));
  }

  private static boolean alreadyRegisteredWith(PluginRegistry registry) {
    final String key = GeneratedPluginRegistrant.class.getCanonicalName();
    if (registry.hasPlugin(key)) {
      return true;
    }
    registry.registrarFor(key);
    return false;
  }
}
