package com.cpa.project.Utils;

import com.cpa.project.Entities.Actors.Mobs.FlyingBat;
import com.cpa.project.Entities.Actors.Mobs.Skeleton;

public class PoolManager {
    private static final EntityPool<Skeleton> skeletonPool = new EntityPool<>(Skeleton.class);
    private static final EntityPool<FlyingBat> batPool = new EntityPool<>(FlyingBat.class);

    public static Skeleton obtainSkeleton() {
        return skeletonPool.obtain();
    }

    public static void freeSkeleton(Skeleton skeleton) {
        skeletonPool.free(skeleton);
    }

    public static FlyingBat obtainBat() {
        return batPool.obtain();
    }

    public static void freeBat(FlyingBat Bat) {
        batPool.free(Bat);
    }
}
