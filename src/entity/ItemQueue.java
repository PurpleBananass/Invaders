package entity;


/** 현재 사용자가 보유한 아이템을 저장하는 Queue */
public class ItemQueue {

    public final Item[] itemQue;
    private final int capacity;
    private int front;
    private int rear;
    private int size;

    public ItemQueue(){
        this.capacity = 3;
        itemQue = new Item[capacity];
        this.front = this.rear = size = 0;
    }

    public void enque(Item item){
        if(size >= capacity) return;
        else {
            itemQue[rear++] = item;
            size++;
        }
        if(rear == capacity)
            rear = 0;
    }

    public Item deque(){
        Item x = itemQue[front++];
        if(size != 0)
            size--;
        if(front == capacity)
            front = 0;
        return x;
    }

    public int getSize(){return this.size;}

    public Item[] getItemQue(){return this.itemQue;}

}
