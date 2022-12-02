package org.course.application;

import org.jetbrains.annotations.NotNull;

public record Order(int clientId, @NotNull String orderId, double startTime) {
}