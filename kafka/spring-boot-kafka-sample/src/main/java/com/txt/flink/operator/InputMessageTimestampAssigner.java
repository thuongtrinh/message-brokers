package com.txt.flink.operator;

import com.txt.flink.model.InputMessage;
import org.apache.flink.api.common.eventtime.*;

import java.time.Duration;

public class InputMessageTimestampAssigner implements WatermarkStrategy<InputMessage> {

    @Override
    public WatermarkGenerator<InputMessage> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
        return new BoundedOutOfOrdernessWatermarks<>(Duration.ofMillis(0)) {

            @Override
            public void onEvent(InputMessage event, long eventTimestamp, WatermarkOutput output) {
                super.onEvent(event, eventTimestamp, output);
                super.onPeriodicEmit(output);
            }
        };
    }

    @Override
    public TimestampAssigner<InputMessage> createTimestampAssigner(TimestampAssignerSupplier.Context context) {
        return WatermarkStrategy.super.createTimestampAssigner(context);
    }

//    @Override
//    public long extractTimestamp(InputMessage element, long previousElementTimestamp) {
//        ZoneId zoneId = ZoneId.systemDefault();
//        return element.getSentAt()
//                .atZone(zoneId)
//                .toEpochSecond() * 1000;
//    }

//    @Nullable
//    @Override
//    public Watermark checkAndGetNextWatermark(InputMessage inputMessage, long extractedTimestamp) {
//        return new Watermark(extractedTimestamp - 15);
//    }

}
