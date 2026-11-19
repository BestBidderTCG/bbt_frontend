package com.gps_g6.bestbiddertcg.core.network

import org.junit.Assert.*
import org.junit.Test

class ImageUrlResolverTest {

    @Test
    fun `valid upload path resolves to backend URL`() {
        val result = NetworkModule.resolveImageUrl(
            "/uploads/piplup.jpg"
        )

        assertEquals(
            "http://10.0.2.2:8080/uploads/piplup.jpg",
            result
        )
    }

    @Test
    fun `null and empty image references return null`() {
        assertNull(NetworkModule.resolveImageUrl(null))
        assertNull(NetworkModule.resolveImageUrl(""))
        assertNull(NetworkModule.resolveImageUrl("   "))
    }

    @Test
    fun `filename without uploads path is rejected`() {
        assertNull(
            NetworkModule.resolveImageUrl("piplup.jpg")
        )
    }

    @Test
    fun `external URLs are rejected`() {
        assertNull(
            NetworkModule.resolveImageUrl(
                "https://example.com/image.jpg"
            )
        )
    }

    @Test
    fun `path traversal is rejected`() {
        assertNull(
            NetworkModule.resolveImageUrl(
                "/uploads/../private/file.jpg"
            )
        )

        assertNull(
            NetworkModule.resolveImageUrl(
                "/uploads/..%2Fprivate/file.jpg"
            )
        )
    }

    @Test
    fun `query strings fragments and backslashes are rejected`() {
        assertNull(
            NetworkModule.resolveImageUrl(
                "/uploads/card.jpg?token=123"
            )
        )

        assertNull(
            NetworkModule.resolveImageUrl(
                "/uploads/card.jpg#fragment"
            )
        )

        assertNull(
            NetworkModule.resolveImageUrl(
                "/uploads\\card.jpg"
            )
        )
    }
}
