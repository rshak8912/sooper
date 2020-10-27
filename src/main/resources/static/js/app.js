function readURL(input, idNum) {
    if (input.files && input.files[0]) {
        let reader = new FileReader();

        reader.onload = function (e) {
            $("#imgPreview" + idNum).attr("src", e.target.result).width(100).height(100);
        }

        reader.readAsDataURL(input.files[0]);
    }
}
$("a.addItem").click(function (e) {
    e.preventDefault();

    let $this = $(this);

    $this.next().removeClass('d-none');

    let id = $this.attr("data-id");
    let url = "/cart/add/"+id;

    $.get(url, {}, function (data) {
        $('div.cart').html(data);
    }).done(function () {
        $this.parent().parent().find('div.productAdded').fadeIn();
        $this.next().addClass('d-none');
        setTimeout(() => {
            $this.parent().parent().find('div.productAdded').fadeOut();
        }, 1000);
    });
});