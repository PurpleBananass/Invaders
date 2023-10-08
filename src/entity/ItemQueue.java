package entity;


/** 현재 사용자가 보유한 아이템을 저장하는 Queue */
public class ItemQueue {

    private final Item[] itemQue;
    private final int capacity;
    private int front;
    private int rear;
    private int size;

    public ItemQueue(){
        this.capacity = 3;
        itemQue = new Item[capacity];
        this.front = this.rear = this.size = 0;
    }

    public void enque(Item item){
        if(size >= capacity){
            deque();
            for(int i=1; i<3; i++)
                itemQue[i-1] = itemQue[i];
            itemQue[2] = null;
        }
        itemQue[rear++] = item;
        size++;
        if(rear == capacity)
            rear = 0;
    }

    public Item deque(){
        Item x = itemQue[front++];
        size--;
        if(front == capacity)
            front = 0;
        return x;
    }

}
