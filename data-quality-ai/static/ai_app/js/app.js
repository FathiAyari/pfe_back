const extractor_base_url = `/extractor/api/v1`
const app = new Vue({
    el: '#app',
    delimiters: ['[[', ']]'],
    data: {
        displayedResults: [],
        results: [],
        acceptedRecords: [],
        tags: ["PIECE", "DIMENSION", "VOLUME", "MASS", "WATT", "OFFER", "STORAGE", "INCH", "COLOR", "SIZE", "VOLT"]
    },
    methods: {
        getExtractionResults() {
            axios({
                url: `${extractor_base_url}/extractEntities`,
                method: 'get',
                params: {
                    nbrRecords: prompt('Products to display:', '12'),
                    token: sessionStorage.getItem('token'),
                }
            })
                .then(response => {
                    this.results = response.data
                    this.chunkResults()
                    this.$refs.loading.hidden = true
                    this.$refs.loaded.hidden = false
                })
                .catch((err) => {
                    console.log(err)
                    if (err.response.status === 403) {
                        alert(`${err.response.data} Please try login again!`)
                        auth()
                    }
                    if (err.response.status === 404) alert('Ai Model doesn\'t exists or under training.\nPlease verify the model or try again after 5 minutes!')
                })
        },
        updateAcceptedRecordsDB() {
            // update all acceptedRecords list in DB with extracted Entities
            axios({
                url: `${extractor_base_url}/update/`,
                method: 'post',
                data: {
                    data: this.acceptedRecords,
                    token: sessionStorage.getItem('token'),
                }
            })
                .then(res => {
                    console.log(res)
                })
                .catch(err => {
                    console.log(err)
                    if (err.response.status === 403) {
                        alert(`${err.response.data} Please try login again!`)
                        auth()
                    }
                })
        },
        addEntity(record) {
            record.entities.push({
                'entity': null,
                'label': null
            })
        },
        saveChanges(record, toAccept = true) {
            // Filter the record from the empty entity if there's ones
            record.entities = record.entities.filter(
                entity => {
                    if (
                        (entity.entity !== null || entity.entity !== '')
                        && (entity.label !== null || entity.label !== '')
                        && record.text.indexOf(entity.entity) !== -1
                    ) return entity
                }
            )
            // emptying the annotations array and fill it with new data
            record.annotations = []
            record.entities.map(
                entity => {
                    let idx = record.text.indexOf(entity.entity)
                    record.annotations.push([
                        idx,
                        idx + entity.entity.length - 1,
                        entity.label
                    ])
                }
            )
            if (toAccept) this.acceptEntity(record)
            this.closeModal(record, false)
        },
        acceptEntity(record) {
            this.acceptedRecords.push(record)
            record.isAccepted = true
        },
        deleteEntity(record, entity) {
            let idx = record.entities.indexOf(entity)
            record.entities.splice(idx, 1)
            record.annotations.splice(idx, 1)
        },
        restoreEntity(record) {
            this.acceptedRecords.splice(this.acceptedRecords.indexOf(record), 1)
            record.isAccepted = false
        },
        cleanUpData() {
            this.acceptedRecords = this.acceptedRecords.map(
                record => {
                    record = {
                        '_id': record._id,
                        'text': record.text,
                        'entities': [...record['entities'].reduce((map, obj) => map.set(obj.entity, obj), new Map()).values()],
                        'image': record.image,
                        'isAccepted': record.isAccepted,
                    }
                    record.annotations = record.entities.map(
                        entity => {
                            let idx = record.text.indexOf(entity.entity)
                            return [idx, idx + entity.entity.length - 1, entity.label]
                        }
                    )
                    return record
                }
            )
        },
        updateModel() {
            this.cleanUpData() // remove duplicated entities if exists
            this.updateAcceptedRecordsDB()
            this.results = this.results.filter(el => this.acceptedRecords.indexOf(el) === -1)
            this.reloadData()
            alert('Model is updating it will take few minutes!')
        },
        reloadData() {
            this.displayedResults = []
            this.chunkResults()
        },
        closeModal(record, toSave = true) {
            if (toSave) this.saveChanges(record)
            $(`#id_${record._id}`).modal('hide')
        },
        chunkResults() {
            // For Display 3 columns Grid Layout
            for (let i = 0; i < this.results.length; i += 3) {
                this.displayedResults.push(this.results.slice(i, i + 3))
            }
        }
    },
    created() {
        auth()
        setTimeout(() => {
            this.getExtractionResults()
        }, 500)
    }
})