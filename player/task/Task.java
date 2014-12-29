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

	int executionTimeRecursive(Creature c) {
		if (taskChain == null) {
			return _executionTime();
		} else {
			return this.location.distance(taskChain.location)/c.speed +
					taskChain.executionTime() + this._executionTime();
		}
	}

	public int executionTime() {
		return creature.location().distance(this.location)+
			this.executionTimeRecursive(creature);
	}

	public int executionTime(Creature c) {
		return c.location().distance(this.location)+
			this.executionTimeRecursive(c);
	}

	public int extraTime(Task task) {
		Task t1 = this;
		Task t2 = task;
		Block cur = creature.location();
		int time = 0;
		while ((t1 != null) && (t2 != null)) {
			if (cur.distance(t1.location)/creature.speed >
				cur.distance(t2.location)/creature.speed) {
				time += cur.distance(t2.location)/creature.speed + t2._executionTime();
				t2 = t2.taskChain;
			} else {
				time += cur.distance(t1.location)/creature.speed + t1._executionTime();
				t1 = t1.taskChain;
			}
		}
		if (t1 != null) {
			time += cur.distance(t1.location)/creature.speed;
			time += t1.executionTimeRecursive(creature);
		} else {
			time += cur.distance(t2.location)/creature.speed;
			time += t2.executionTimeRecursive(creature);
		}
		return time - this.executionTime();
	}

	public Task merge(Task task) {
		Task t1 = this;
		Task t2 = task;
		Block cur = creature.location();
		Task res = null;
		if (cur.distance(t1.location)/creature.speed >
			cur.distance(t2.location)/creature.speed) {
			res = t2;
			t2 = t2.taskChain;
		} else {
			res = t1;
			t1 = t1.taskChain;
		}
		while ((t1 != null) && (t2 != null)) {
			if (cur.distance(t1.location)/creature.speed >
				cur.distance(t2.location)/creature.speed) {
				res.taskChain = t2;
				t2 = t2.taskChain;
			} else {
				res.taskChain = t1;
				t1 = t1.taskChain;
			}
		}
		if (t1 != null) {
			res.taskChain = t1;
		} else {
			res.taskChain = t2;
		}
		return res;
	}
}
