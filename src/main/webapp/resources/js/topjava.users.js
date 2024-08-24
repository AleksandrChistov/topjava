const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl,
    updateTable: function updateTable() {
        $.get(ctx.ajaxUrl, function (data) {
            ctx.datatableApi.clear().rows.add(data).draw();
        });
    }
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
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
                    "asc"
                ]
            ]
        })
    );
});

function toggleStatus(id, input) {
    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl + `${id}`,
        data: {enabled: input.checked}
    }).done(function () {
        $(input).parents("tr").toggleClass("text-disabled");
        successNoty("Changed");
    }).fail(function (jqXHR) {
        $(input).prop('checked', !input.checked);
        failNoty(jqXHR);
    });
}