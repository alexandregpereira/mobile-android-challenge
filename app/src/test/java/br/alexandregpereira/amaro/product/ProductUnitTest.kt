package br.alexandregpereira.amaro.product

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ProductUnitTest {

    private val viewModel = ProductTestViewModel()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun getProducts_listIsAllRight() {
        val products = viewModel.getProductsLiveData().value

        assertEquals(false, viewModel.getLoadingLiveData().value)
        assertTrue(products?.isNotEmpty() ?: false)
        if (products == null) return
        assertEquals(22, products.size)
        products[0].apply {
            assertTrue(name.isNotEmpty())
            assertFalse(onSale)
            assertTrue(regularPrice.isNotEmpty())
            assertTrue(actualPrice.isNotEmpty())
            assertTrue(discountPercentage.isEmpty())
            assertTrue(installments.isNotEmpty())
            assertTrue(image.isNotEmpty())

            assertEquals(5, sizes.size)
            sizes[1].apply {
                assertTrue(available)
                assertFalse(size.isNullOrEmpty())
                assertFalse(sku.isNullOrEmpty())
            }
        }
    }

    @Test
    fun getProductByName_objIsAllRight() {
        getProducts_listIsAllRight()
        val product = viewModel.getProductByName("T-SHIRT LEATHER DULL")
        assertNotNull(product)
        if (product == null) return

        product.apply {
            assertEquals("T-SHIRT LEATHER DULL", name)
            assertTrue(onSale)
            assertTrue(regularPrice.isNotEmpty())
            assertTrue(actualPrice.isNotEmpty())
            assertTrue(discountPercentage.isNotEmpty())
            assertTrue(installments.isNotEmpty())
            assertTrue(image.isEmpty())

            assertEquals(5, sizes.size)
            sizes[1].apply {
                assertTrue(available)
                assertFalse(size.isNullOrEmpty())
                assertFalse(sku.isNullOrEmpty())
            }
        }
    }
}
