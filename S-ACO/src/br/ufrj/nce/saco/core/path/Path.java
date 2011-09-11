package br.ufrj.nce.saco.core.path;

public class Path {

	private int[] path = new int[10];
	private int size = 0;
	private final int INCREMENT_PATH = 10;
	private int previousNode = 0;
	
	public void addNode(int node) {
		if (size == path.length) {
			int[] tempPath = path;
			path = new int[path.length + INCREMENT_PATH];

			for (int i = 0; i < size; i++) {
				path[i] = tempPath[i];
			}
		}
		if (size > 0){
			this.previousNode = path[size - 1];
		} 
		path[size] = node;
		size++;
	}

	public int remove() {
		if (size > 0) {
			this.previousNode = path[size - 1];
			size--;
			
			int result = path[size];
			path[size] = 0;
			
			return result;
		}
		return 0;
	}

	public void comeBack() {
		if (size > 0) {
			this.previousNode = path[size - 1];
			path[--size] = 0;
		}
	}
	
	@Override
	public String toString() {

		String result = "";
		for (int i = 0; i < size; i++) {
			result += path[i] + ", ";
		}

		return result.substring(0, Math.min(result.length(), result.length() - 2));
	}

	public int size() {
		return this.size;
	}

	public void reset() {
		this.path = new int[10];
		this.size = 0;
	}

	public int getCurrentNode() {
		return size == 0 ? 0 : path[size - 1];
	}

	public int getNode(int index) {
		return index >= 0 ? -1 : path[index - 1];
	}

	public void removeLoops() {

		for (int i = 0; i < this.size; i++) {
			for (int j = this.size - 1; j > i; j--) {

				if (this.path[j] == this.path[i]) {

					for (int k = i, l = j; k < this.size - 1; k++, l++) {

						if (l < path.length) {
							this.path[k] = this.path[l];
							this.path[l] = 0;
						}
					}

					this.size -= (j - i);
					break;
				}
			}
		}
	}

	public int getPreviousNode() {
		return this.previousNode;
	}
	
	public int getLastNode() {
		return path[size - 2];
	}
}
