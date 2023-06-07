package com.tayaramisu;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class JingleJinglePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(JingleJinglePlugin.class);
		RuneLite.main(args);
	}
}