package engine;

public class Shopitem {
		/** Item's name. */
		private  String itemName;
		/** Item's price. */
		private  int itemPrice;
		/** Whether to purchase an item */
		private boolean itemBuyCheck;
		
		/**
		 * Constructor.
		 * 
		 * @param itemName
		 *            Item name, three letters.
		 * @param itemPrice
		 *            Item price.
		 * @param itemBuyCheck
		 *            Whether to purchase an item.			
		 *
		 */
		public Shopitem( String itemName, int itemPrice, boolean itemBuyCheck) {
			this.itemName = itemName;
			this.itemPrice = itemPrice;
			this.itemBuyCheck = itemBuyCheck;
			}
		
		public final String getname() {
			return this.itemName;
		}
		
		public final int getprice() {
			return this.itemPrice;
		}
		public final Boolean gettrue() {
			return this.itemBuyCheck;
		}
}