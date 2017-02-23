package com.mymdsmanager.task;

public interface CompleteListener {

	/**
	 * result is generic object type, it may be casted into specific type as needed.
	 * @param result
	 */
	public void onRemoteCallComplete(Object result);
	public void onRemoteErrorOccur(Object error);

}
