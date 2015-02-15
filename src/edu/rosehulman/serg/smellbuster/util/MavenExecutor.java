package edu.rosehulman.serg.smellbuster.util;

import java.io.File;
import java.util.Collections;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

public class MavenExecutor {

	public static void executeMavenTask(String buildFilePath) {
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile(new File(buildFilePath));
		request.setGoals(Collections.singletonList("install"));
		
		Invoker invoker = new DefaultInvoker();
		try {
			invoker.execute(request);
		} catch (MavenInvocationException e) {
			e.printStackTrace();
		}
	}

}
