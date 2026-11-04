function loadGroups() {
    let facultyId = $('#faculty-select').val();
    let course = $('#course-select').val();
    console.log(facultyId);
    console.log(course);
    if (facultyId && course) {
        console.log("ajax started")
        $.ajax({
            type: "GET",
            url: contextPath + "/api/groups",
            data: {
                facultyId: facultyId,
                course: course
            },
            success: function (groups) {
                let groupSelect = $('#group-select');
                groupSelect.empty();
                groupSelect.append('<option value="" disabled selected>Выберите группу...</option>');

                for (let i = 0; i < groups.length; i++) {
                    groupSelect.append('<option value="' + groups[i].id + '">' + groups[i].name + '</option>');
                }
            },
            dataType: "json"
        });
    }
}
$(document).ready(function() {
    $('#faculty-select').change(loadGroups);
    $('#course-select').change(loadGroups);
})
