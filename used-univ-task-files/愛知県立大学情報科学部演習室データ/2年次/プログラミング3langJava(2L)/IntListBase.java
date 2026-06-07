class IntListBase implements IntSetBase{
	public IntNodeBase head;
	
	public IntListBase() {
		head = null;
	}
	
	public void add(int value, String name) {
		IntNodeBase node = new IntNodeBase(value, name);
		node.next = head;
		head = node;
	}
	
	public boolean findName(String name) {
		IntNodeBase node = head;
		while(node != null) {
			if(node.name.equals(name)) {
				return true;
			} else {
				node = node.next;
			}
		}
		return false;
	}
}

class IntNodeBase{
	public int value;
	public String name;
	public IntNodeBase next;
	
	public IntNodeBase(int x, String y) {
		value = x;
		name = y;
		next = null;
	}
}

		