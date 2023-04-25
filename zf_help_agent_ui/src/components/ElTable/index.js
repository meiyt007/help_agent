import { Table } from "element-ui";

export default {
    extends: Table,
    props: {
        stripe: {
            type: Boolean,
            default: true
        }
    }
}