package com.eresearch.author.matcher.application.event.listener;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

import lombok.extern.log4j.Log4j;

/**
 * This listener listens for the event which is produced when the environment is
 * known.
 * 
 * @author chriniko
 *
 */
@Log4j
public class ApplicationEnvironmentPreparedEventListener
		implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

	@Override
	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
		// NOTE: add functionality according to your needs...
		log.info("~~~Application Environment Initialized(known)~~~");
	}

}
