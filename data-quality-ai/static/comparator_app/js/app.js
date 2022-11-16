const comparator_base_url = '/comparator/api/v1'
const app = new Vue({
    el: '#app',
    delimiters: ['[[', ']]'],
    data: {
        products: [],
        categories: [],
        brands: [],
    },
    methods: {
        getComparisonResults() {
            axios({
                url: `${comparator_base_url}/compare`,
                method: 'get',
                params: {
                    token: sessionStorage.getItem('token'),
                }
            })
                .then(
                    (response) => {
                        this.products = response.data.products
                        this.categories = response.data.categories
                        this.brands = response.data.brands
                        this.$refs.loading.hidden = true
                        this.$refs.loaded.hidden = false
                    }
                )
                .catch(
                    (error) => {
                        console.log(error)
                        if (error.response.status === 403) {
                            alert(`${error.response.data} Please try login again!`)
                            auth()
                        }
                    }
                )
        },
        mergeProducts(products) {
            axios({
                url: `${comparator_base_url}/merge`,
                method: 'post',
                data: {
                    products: products,
                    token: sessionStorage.getItem('token'),
                },
            })
                .then((response) => {
                    console.log(response)
                })
                .catch((error) => {
                    console.log(error)
                    if (error.response.status === 403) {
                        alert(`${error.response.data} Please try login again!`)
                        auth()
                    }
                })
        },
        acceptSimilarProductsAndMerge(product, productIdx, category, brand) {
            let acceptedProducts = [product.id]
            let atLeastOneChecked = false
            product.similar.map(
                (similarProduct) => {
                    let ref = `checkbox_${productIdx}_${product.id}_${similarProduct.product.id}`
                    if (this.$refs[ref][0].checked) {
                        if (!atLeastOneChecked) atLeastOneChecked = true
                        acceptedProducts.push(similarProduct.product.id)
                    }
                }
            )

            if (!atLeastOneChecked) {
                alert('You must select at least one product to merge')
                return
            }

            if (confirm('Are you sure you want to merge the products?')) {


                this.products[category][brand] = this.products[category][brand].filter(
                    (product) => {
                        return acceptedProducts.indexOf(product.id) === -1
                    }
                )

                let categoryIdx = this.categories.indexOf(category)
                let brandIdx = this.brands[categoryIdx].indexOf(brand)

                if (!this.products[category][brand].length) {
                    this.brands[categoryIdx].splice(brandIdx, 1)
                }

                if (!this.brands[categoryIdx].length) {
                    this.brands.splice(categoryIdx, 1)
                    this.categories.splice(categoryIdx, 1)
                }

                this.mergeProducts(acceptedProducts)
                product.similar.map(
                    (similarProduct) => {
                        let ref = `checkbox_${productIdx}_${product.id}_${similarProduct.product.id}`
                        if (this.$refs[ref][0].checked) this.$refs[ref][0].checked = false

                    }
                )
            }
        },
    },
    created() {
        auth()
        setTimeout(() => {
            this.getComparisonResults()
        }, 500)
    }
})