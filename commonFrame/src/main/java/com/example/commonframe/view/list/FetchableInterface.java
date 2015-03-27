package com.example.commonframe.view.list;

public interface FetchableInterface {
	/**
	 * public interface<br>
	 * <b>OnLoadMoreListener</b> <br>
	 * <b>Class Overview</b> <br>
	 * <br>
	 * Interface to implement when you want to get notified of 'pull to refresh'
	 * events. Call setOnLoadMoreListener(..) to activate an OnLoadMoreListener.
	 */
	public interface OnLoadMoreListener {
		/**
		 * Called when the list reaches the last item (the last item is visible
		 * to the user)
		 */
		public void onLoadMore();

		/**
		 * Method to indicate whether this action will override 'refresh' event.
		 * Users must cancel relevant actions such as connections, threads, data
		 * transferring when return <code>true</code> to avoid conflicting in
		 * data with 'refresh' event.
		 * 
		 * @return true if this 'load more' should override 'refresh' when both
		 *         are triggered. Return false otherwise.
		 * 
		 */
		public boolean shouldOverrideRefresh();
	}

	public interface OnRefreshListener {

		/**
		 * Method to be called when user pull over the first item of the list
		 * and a refresh is requested
		 */
		public void onRefresh();

		/**
		 * Method to indicate whether this action will override 'load more'
		 * event. Users must cancel relevant actions such as connections,
		 * threads, data transferring when return <code>true</code> to avoid
		 * conflicting in data with 'load more' event.
		 * 
		 * @return true if this 'refresh' should override 'load more' when both
		 *         are triggered. Return false otherwise.
		 * 
		 */
		public boolean shouldOverrideLoadMore();
	}

	public enum State {
		PULL_TO_REFRESH, RELEASE_TO_REFRESH, REFRESHING
	}
}
