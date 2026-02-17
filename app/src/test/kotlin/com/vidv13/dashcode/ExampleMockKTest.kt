package com.vidv13.dashcode

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExampleMockKTest {
    interface Greeter {
        fun greet(): String
    }

    @Test
    fun `mockk works`() {
        val greeter = mockk<Greeter>()
        every { greeter.greet() } returns "Hello, DashCode!"
        assertEquals("Hello, DashCode!", greeter.greet())
    }
}
