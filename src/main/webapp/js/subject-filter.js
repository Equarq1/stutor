function loadSubjects() {
    let facultyId = $("#faculty-select").val();
    let course = $("#course-select").val();

    if (facultyId && course) {
        $.ajax({
            type: "GET",
            url: contextPath + "/api/subjects",
            data: {
                facultyId: facultyId,
                course: course
            },
            dataType: "json",
            success: function (subjects) {
                let container = $("#subjectsContainer");

                container.find(".subject-chip:not(:has(input:checked))").remove();
                container.find(".placeholder-text").remove();

                if (subjects.length === 0 && container.children().length === 0) {
                    container.append('<span class="placeholder-text">Для этого курса предметов не найдено</span>');
                    return;
                }

                for (let i = 0; i < subjects.length; i++) {
                    if (container.find('input[value="' + subjects[i].id + '"]').length === 0) {
                        container.append(
                            '<label class="subject-chip">' +
                            '<input type="checkbox" name="subjectIds" value="' + subjects[i].id + '">' +
                            '<span>' + subjects[i].name + '</span>' +
                            '</label>'
                        );
                    }
                }
            }
        });
    }
}

$(document).ready(function() {
    $('#faculty-select').change(loadSubjects);
    $('#course-select').change(loadSubjects);
})