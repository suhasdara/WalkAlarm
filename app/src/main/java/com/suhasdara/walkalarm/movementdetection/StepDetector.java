package com.suhasdara.walkalarm.movementdetection;

public class StepDetector {
    private static final int ACC_RING_SIZE = 50;
    private static final int VEL_RING_SIZE = 10;

    // change this threshold according to your sensitivity preferences
    private static final float STEP_THRESHOLD = 10f;

    private static final int STEP_DELAY_NS = 250000000;

    private int accRingCounter = 0;
    private float[] accRingX = new float[ACC_RING_SIZE];
    private float[] accRingY = new float[ACC_RING_SIZE];
    private float[] accRingZ = new float[ACC_RING_SIZE];
    private int velRingCounter = 0;
    private float[] velRing = new float[VEL_RING_SIZE];
    private long lastStepTimeNs = 0;
    private float oldVelocityEstimate = 0;

    private StepListener listener;

    public void registerListener(StepListener listener) {
        this.listener = listener;
    }

    public void updateAcc(long timeNs, float x, float y, float z) {
        float[] currentAcc = new float[3];
        currentAcc[0] = x;
        currentAcc[1] = y;
        currentAcc[2] = z;

        // First step is to update our guess of where the global z vector is.
        accRingCounter++;
        accRingX[accRingCounter % ACC_RING_SIZE] = currentAcc[0];
        accRingY[accRingCounter % ACC_RING_SIZE] = currentAcc[1];
        accRingZ[accRingCounter % ACC_RING_SIZE] = currentAcc[2];

        float[] worldZ = new float[3];
        worldZ[0] = sum(accRingX) / Math.min(accRingCounter, ACC_RING_SIZE);
        worldZ[1] = sum(accRingY) / Math.min(accRingCounter, ACC_RING_SIZE);
        worldZ[2] = sum(accRingZ) / Math.min(accRingCounter, ACC_RING_SIZE);

        float normalization_factor = norm(worldZ);

        worldZ[0] = worldZ[0] / normalization_factor;
        worldZ[1] = worldZ[1] / normalization_factor;
        worldZ[2] = worldZ[2] / normalization_factor;

        float currentZ = dot(worldZ, currentAcc) - normalization_factor;
        velRingCounter++;
        velRing[velRingCounter % VEL_RING_SIZE] = currentZ;

        float velocityEstimate = sum(velRing);

        if (velocityEstimate > STEP_THRESHOLD && oldVelocityEstimate <= STEP_THRESHOLD && (timeNs - lastStepTimeNs > STEP_DELAY_NS)) {
            listener.step(timeNs);
            lastStepTimeNs = timeNs;
        }
        oldVelocityEstimate = velocityEstimate;
    }

    private static float sum(float[] array) {
        float retVal = 0;
        for (float val : array) {
            retVal += val;
        }
        return retVal;
    }

    private static float norm(float[] array) {
        float retVal = 0;
        for (float val : array) {
            retVal += val;
        }
        return (float) Math.sqrt(retVal);
    }


    private static float dot(float[] arrayA, float[] arrayB) {
        return (arrayA[0] * arrayB[0]) + (arrayA[1] * arrayB[1]) + (arrayA[2] * arrayB[2]);
    }

    public interface StepListener {
        void step(long timeNs);
    }
}
