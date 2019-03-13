package br.alexandregpereira.amaro.product

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.alexandregpereira.amaro.ui.product.list.ProductsOrder
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
        viewModel.getProductsLiveData()
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

    @Test
    fun orderByLowPrice() {
        viewModel.getProductsLiveData()
        viewModel.orderBy(ProductsOrder.LOW_HIGH)

        val products = viewModel.getProducts()
        assertNotNull(products)
        if (products == null) return
        assertEquals("PULSEIRA STYLISH", products.first().name)
        assertEquals("CASACO WHITE FUR", products.last().name)
    }

    @Test
    fun orderByLowPrice_refreshAfter() {
        viewModel.getProductsLiveData()
        viewModel.orderBy(ProductsOrder.LOW_HIGH)
        viewModel.refresh()

        val products = viewModel.getProducts()
        assertNotNull(products)
        if (products == null) return
        assertEquals("PULSEIRA STYLISH", products.first().name)
        assertEquals("CASACO WHITE FUR", products.last().name)
    }

    @Test
    fun orderByHighPrice() {
        viewModel.getProductsLiveData()
        viewModel.orderBy(ProductsOrder.HIGH_LOW)

        val products = viewModel.getProducts()
        assertNotNull(products)
        if (products == null) return
        assertEquals("CASACO WHITE FUR", products.first().name)
        assertEquals("PULSEIRA STYLISH", products.last().name)
    }
    @Test
    fun orderByHighPrice_refreshAfter() {
        viewModel.getProductsLiveData()
        viewModel.orderBy(ProductsOrder.HIGH_LOW)
        viewModel.refresh()

        val products = viewModel.getProducts()
        assertNotNull(products)
        if (products == null) return
        assertEquals("CASACO WHITE FUR", products.first().name)
        assertEquals("PULSEIRA STYLISH", products.last().name)
    }

    @Test
    fun orderByAny_whenIsNotAnyOrder() {
        viewModel.getProductsLiveData()
        viewModel.orderBy(ProductsOrder.HIGH_LOW)
        viewModel.orderBy(ProductsOrder.ANY)

        val products = viewModel.getProducts()
        assertNotNull(products)
        if (products == null) return
        assertEquals("VESTIDO TRANSPASSE BOW", products.first().name)
        assertEquals("PULSEIRA STYLISH", products.last().name)
    }

    @Test
    fun orderByAny_whenIsNotAnyOrder_refreshAfter() {
        viewModel.getProductsLiveData()
        viewModel.orderBy(ProductsOrder.HIGH_LOW)
        viewModel.orderBy(ProductsOrder.ANY)
        viewModel.refresh()

        val products = viewModel.getProducts()
        assertNotNull(products)
        if (products == null) return
        assertEquals("VESTIDO TRANSPASSE BOW", products.first().name)
        assertEquals("PULSEIRA STYLISH", products.last().name)
    }

}
