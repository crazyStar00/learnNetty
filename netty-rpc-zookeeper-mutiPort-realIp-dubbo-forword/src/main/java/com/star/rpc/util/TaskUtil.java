package com.star.rpc.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskUtil {
   public static  ExecutorService executorService = Executors.newCachedThreadPool();
}
