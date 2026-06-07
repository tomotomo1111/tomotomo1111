class IntList2 implements IntSet2{
	public IntNode2 head;
	public IntList2() {
		head = null;
	}
	
	public void add(int value, String name) {
		IntNode2 node = new IntNode2(value, name);
		node.next = head;
		head = node;
	}
	
	public String getName(int index) {
		IntNode2 node = head;
		while(index-- > 0) {
			node = node.next;
		}
		return node.name;
	}
	
	public boolean findName(String name) {
		IntNode2 node = head;
		while(node != null) {
			if(node.name.equals(name)) {
				return true;
			} else {
				node = node.next;
			}
		}
		return false;
	}
	
	public int size() {
		int s = 0;
		IntNode2 node = head;
		while(node != null) {
			s++;
			node = node.next;
		}
		return s;
	}
}

class IntNode2 {
	public int value;
	public String name;
	public IntNode2 next;
	public IntNode2(int x, String y) {
		value = x;
		name = y;
		next = null;
	}
}