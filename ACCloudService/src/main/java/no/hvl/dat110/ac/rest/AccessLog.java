package no.hvl.dat110.ac.rest;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;

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
		
		Gson gson = new Gson();
		String json = null;
		ConcurrentHashMap<Integer, AccessEntry> clone = new ConcurrentHashMap<Integer, AccessEntry>(log);
		
		json += "[";
		if (!log.isEmpty()) {
			
			Iterator<Entry<Integer, AccessEntry>> it = clone.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, AccessEntry> entry = it.next();
				json += gson.toJson(entry.getValue());
				it.remove();
				if (it.hasNext()) {
					json += ",";
				}
			}
			
		}
		json += "]";

		return json;
	}
}
