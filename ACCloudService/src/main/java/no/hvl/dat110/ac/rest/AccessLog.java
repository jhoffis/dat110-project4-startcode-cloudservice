package no.hvl.dat110.ac.rest;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * What is the goal this class has? The cloud-service should make it possible
 * for the access control device to register attempts to access the system in an
 * access log
 * 
 * @author Jhoffis
 *
 */
public class AccessLog {

	private AtomicInteger cid;
	protected ConcurrentHashMap<Integer, AccessEntry> log;

	public AccessLog() {
		this.log = new ConcurrentHashMap<Integer, AccessEntry>();
		cid = new AtomicInteger(0);
	}

	// add an access entry for the message and return assigned id
	public int add(String message) {

		int id = cid.getAndIncrement();
		AccessEntry entry = new AccessEntry(id, message);
		log.put(id, entry);
		return id;
	}

	// retrieve a specific access entry
	public AccessEntry get(int id) {

		return log.get(id);

	}

	// clear the access entry log
	public void clear() {
		log.clear();
	}

	/**
	 * 	JSON representation of the access log
	 * 
	 * @return noe som dette:
	 * [{"id":1,"message":"locked"},{"id":2,"message": "unlocked"}]
	 */
	public String toJson() {

		String json = null;

		if (!log.isEmpty()) {
			json += "[";
			
			Iterator<Entry<Integer, AccessEntry>> it = log.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, AccessEntry> entry = it.next();
				json += "{\"id\":" + entry.getKey() + ",\"message\":\"" + entry.getValue() + "\"}";
				it.remove();
				if (it.hasNext()) {
					json += ",";
				}
			}
			
			json += "]";
		}

		return json;
	}
}
