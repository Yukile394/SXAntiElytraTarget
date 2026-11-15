package studio.yukilem.yukilem.libs.nixlib.events;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class EventBus {
   private final Map<Class<?>, List<EventBus.Listener<?>>> listeners = new HashMap<>();

   public EventBus() {
   }

   public <T> EventBus on(Class<T> clazz1, Consumer<T> consumer2) {
      return this.on(clazz1, 0, false, consumer2);
   }

   public <T> EventBus on(Class<T> clazz1, int i2, boolean flag3, Consumer<T> consumer4) {
      this.listeners.computeIfAbsent(clazz1, arg0 -> new CopyOnWriteArrayList<>()).add(new EventBus.Listener(consumer4, i2, flag3));
      this.listeners.get(clazz1).sort(Comparator.comparingInt(arg0 -> -arg0.priority));
      return this;
   }

   public EventBus subscribeAll(Object object1) {
      for (Method method5 : object1.getClass().getDeclaredMethods()) {
         Subscribe subscribe6 = method5.getAnnotation(Subscribe.class);
         if (subscribe6 != null) {
            if (method5.getParameterCount() != 1) {
               throw new IllegalArgumentException("@Subscribe method must take exactly one argument: " + method5);
            }

            method5.setAccessible(true);
            Class clazz7 = method5.getParameterTypes()[0];
            this.attach(clazz7, object1, method5, subscribe6);
         }
      }

      return this;
   }

   private void attach(Class<?> clazz1, Object object2, Method method3, Subscribe subscribe4) {
      Consumer consumer5 = arg2x -> {
         try {
            method3.invoke(object2, arg2x);
         } catch (ReflectiveOperationException reflectiveOperationException4x) {
            throw new RuntimeException("event dispatch failed for " + method3.getName(), reflectiveOperationException4x.getCause() == null ? reflectiveOperationException4x : reflectiveOperationException4x.getCause());
         }
      };
      EventBus eventBus6 = this.on(clazz1, subscribe4.priority(), subscribe4.ignoreCancelled(), consumer5);
   }

   public <T> T post(T t1) {
      List<EventBus.Listener> list2 = (List<EventBus.Listener>)(List)this.listeners.get(t1.getClass());
      if (list2 != null && !list2.isEmpty()) {
         for (EventBus.Listener listener4 : list2) {
            if (!(t1 instanceof Cancellable) || !((Cancellable)t1).isCancelled() || listener4.ignoreCancelled) {
               listener4.handler.accept((T)t1);
            }
         }

         return (T)t1;
      } else {
         return (T)t1;
      }
   }

   public void unsubscribe(Class<?> clazz1) {
      this.listeners.remove(clazz1);
   }

   public void clear() {
      this.listeners.clear();
   }

   public int listenerCount() {
      int i1 = 0;

      for (List list3 : this.listeners.values()) {
         i1 += list3.size();
      }

      return i1;
   }

   private record Listener<T>(Consumer<T> handler, int priority, boolean ignoreCancelled) {
   }
}
