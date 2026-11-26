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
                        let label = $('<label>', {class: 'subject-chip'});
                        let checkbox = $('<input>', {
                            type: 'checkbox',
                            name: 'subjectIds',
                            value: subjects[i].id
                        });
                        let name = $('<span>', {text: subjects[i].name});

                        label.append(checkbox, name);
                        container.append(label);
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