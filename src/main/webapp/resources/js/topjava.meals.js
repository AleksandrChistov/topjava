const mealAjaxUrl = "meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function updateTable() {
        $.get(ctx.ajaxUrl + "filter?" + $('#filterForm').serialize(), function(data) {
            ctx.datatableApi.clear().rows.add(data).draw();
        });
    }
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});

function filter() {
    ctx.updateTable();
}

function clean() {
    $('#filterForm').find("input").each(function(i, input) {
        input.value = "";
    });
    ctx.updateTable();
}
