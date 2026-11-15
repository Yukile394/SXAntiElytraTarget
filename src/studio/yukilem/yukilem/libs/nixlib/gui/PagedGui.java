package studio.glace.glantielytratarget.libs.nixlib.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PagedGui extends Gui {
   private final List<GuiItem> entries = new ArrayList<>();
   private final List<Integer> contentSlots = new ArrayList<>();
   private int page;
   private int previousSlot = -1;
   private int nextSlot = -1;
   private ItemStack previousItem;
   private ItemStack nextItem;
   private ItemStack previousFiller;
   private ItemStack nextFiller;

   public PagedGui(String string1, int i2) {
      super(string1, i2);

      for (int i3 = 1; i3 < i2 - 1; i3++) {
         for (int i4 = 1; i4 < 8; i4++) {
            this.contentSlots.add(i3 * 9 + i4);
         }
      }
   }

   public PagedGui contentSlots(int... arg1) {
      this.contentSlots.clear();
      Arrays.stream(arg1).forEach(this.contentSlots::add);
      return this;
   }

   public PagedGui pagination(int i1, ItemStack itemStack2, int i3, ItemStack itemStack4) {
      this.previousSlot = i1;
      this.nextSlot = i3;
      this.previousItem = itemStack2;
      this.nextItem = itemStack4;
      return this;
   }

   public PagedGui paginationFiller(ItemStack itemStack1, ItemStack itemStack2) {
      this.previousFiller = itemStack1;
      this.nextFiller = itemStack2;
      return this;
   }

   public PagedGui addEntry(ItemStack itemStack1) {
      this.entries.add(GuiItem.of(itemStack1));
      return this;
   }

   public PagedGui addEntry(ItemStack itemStack1, Consumer<ClickContext> consumer2) {
      this.entries.add(GuiItem.of(itemStack1, consumer2));
      return this;
   }

   public int currentPage() {
      return this.page;
   }

   public int totalPages() {
      return !this.contentSlots.isEmpty() && !this.entries.isEmpty() ? (int)Math.ceil((double)this.entries.size() / this.contentSlots.size()) : 1;
   }

   public PagedGui open(Player player1, int i2) {
      this.page = Math.max(0, Math.min(i2, this.totalPages() - 1));
      this.render();
      super.open(player1);
      return this;
   }

   public void goTo(int i1) {
      int i2 = Math.max(0, Math.min(i1, this.totalPages() - 1));
      if (i2 != this.page) {
         this.page = i2;
         this.render();
      }
   }

   public void nextPage() {
      this.goTo(this.page + 1);
   }

   public void previousPage() {
      this.goTo(this.page - 1);
   }

   private void render() {
      for (int i2 : this.contentSlots) {
         this.clear(i2);
      }

      int i6 = this.contentSlots.size();
      int i7 = this.page * i6;
      int i3 = Math.min(i7 + i6, this.entries.size());

      for (int i4 = i7; i4 < i3; i4++) {
         int i5 = this.contentSlots.get(i4 - i7);
         this.setItem(i5, this.entries.get(i4));
      }

      if (this.previousSlot >= 0) {
         if (this.page > 0) {
            this.setItem(this.previousSlot, this.previousItem, arg1 -> this.previousPage());
         } else if (this.previousFiller != null) {
            this.setItem(this.previousSlot, this.previousFiller.clone());
         } else {
            this.clear(this.previousSlot);
         }
      }

      if (this.nextSlot >= 0) {
         if (this.page < this.totalPages() - 1) {
            this.setItem(this.nextSlot, this.nextItem, arg1 -> this.nextPage());
         } else if (this.nextFiller != null) {
            this.setItem(this.nextSlot, this.nextFiller.clone());
         } else {
            this.clear(this.nextSlot);
         }
      }
   }
}
