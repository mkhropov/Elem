package task;

public class Task {
	private Task taskChain;
	private Block location;
	private Creature creature;

	public Task(Block location) {
		this.taskChain = null;
		this.location = location;
	}

	public Block expectedLocation() {
		if (taskChain == null) {
			return location;
		} else {
			return taskChain.expectedLocation;
		}
	}


	// Overridable
	int _executionTime() {
		return 0;
	}

	public int executionTime() {
		if (taskChain == null) {
			return _executionTime();
		} else {
			return this.location.distance(taskChain.location)/creature.speed +
					taskChain.executionTime() + _executionTime();
		}
	}

	public int extraTime(Task task) {
		Task t1 = this;
		Task t2 = task;
		while ((t1.taskChain != null) && (t2.taskChain != null)) {
			if (t1.taskChain != null
		}
	}
	public void merge(Task task);
}
