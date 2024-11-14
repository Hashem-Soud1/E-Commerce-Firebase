import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(
    private val spanCount: Int = 1, // العمود أو الصف
    private val spacing: Int, // المسافة
    private val includeEdge: Boolean,
    private val isHorizontal: Boolean = false // التحديد إذا كان أفقي أو عمودي
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column

        if (isHorizontal) {
            // حالة العرض الأفقي
            if (includeEdge) {
                outRect.left = spacing
                outRect.right = spacing
                if (position == 0) {
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 1) * spacing / spanCount
                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        } else {
            // حالة العرض العمودي
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 1) * spacing / spanCount
                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            } else {
                // حساب المسافات بشكل مرن بين الأعمدة
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 1) * spacing / spanCount
                if (position >= spanCount) {
                    outRect.top = spacing // المسافة في الأعلى للصفوف التي ليست الأولى
                }
                if (position % spanCount == 0) {
                    outRect.left = 0 // عدم إضافة المسافة في العمود الأول
                }
            }
        }
    }
}
